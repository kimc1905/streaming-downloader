package me.moonchan.ts.downloader.core;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.core.domain.model.Cookie;
import me.moonchan.ts.downloader.core.domain.model.M3uInfo;
import me.moonchan.ts.downloader.core.domain.url.DownloadUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.GzipSource;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

import static me.moonchan.ts.downloader.core.util.CheckedExceptionLambdaUtil.throwingConsumerWrapper;

@Slf4j
public class DownloadTask {

    public enum State {
        READY, DOWNLOADING, PAUSE, ERROR, COMPLETE
    }

    public static class Progress {

        private Progress(int current, int length) {
            this.current = current;
            this.length = length;
        }

        public static Progress of(int current, int length) {
            return new Progress(current, length);
        }

        @Getter
        private int current;
        @Getter
        private int length;

        public double getPercent() {
            return (double) current / (double) length;
        }
    }

    private static final int RETRY = 3;
    private static final State INIT_DOWNLOAD_STATE = State.READY;

    private OkHttpClient client;
    private DownloadInfo downloadInfo;
    @Getter
    private State state;
    @Getter
    private final Relay<State> observableState = BehaviorRelay.create();
    @Getter
    private final Relay<Progress> observableProgress = PublishRelay.create();

    public DownloadTask(OkHttpClient client, DownloadInfo downloadInfo) {
        this.client = client;
        this.downloadInfo = downloadInfo;
        this.state = INIT_DOWNLOAD_STATE;
    }

    public boolean isFinished() {
        return (state == State.COMPLETE || state == State.ERROR);
    }

    private void createDestFile() throws IOException {
        File destFile = downloadInfo.getDestFile();
        if (destFile.exists())
            destFile.delete();
        destFile.createNewFile();
    }

    private Response requestTo(String url) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Cookie cookie = downloadInfo.getCookie();
        if (cookie != null)
            requestBuilder.addHeader("Cookie", cookie.toString());
        Request request = requestBuilder
                .get()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.63.11 Safari/537.36")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept", "*/*")
                .build();
        return client.newCall(request).execute();
    }

    private Optional<InputStream> downloadM3u8() throws IOException {
        String m3u8Url = downloadInfo.getDownloadUrl().getM3u8Url();
        Response response = requestTo(m3u8Url);
        if (response.isSuccessful()) {
            if (response.body() == null) {
                return Optional.empty();
            }
            return Optional.of(response.body().byteStream());
        } else {
            log.debug("Response code: " + response.code());
            if (response.body() != null)
                log.debug("msg: " + response.body().string());
        }

        return Optional.empty();
    }

    private void downloadFromUrl(String url, File dest) throws IOException {
        downloadFromUrl(url, dest, 1);
    }

    private void downloadFromUrl(String url, File dest, int tryCount) throws IOException {
        try {
            Response response = requestTo(url);
            saveFile(response, dest);
        } catch (Exception e) {
            if (tryCount > RETRY) {
                e.printStackTrace();
                throw e;
            }
            downloadFromUrl(url, dest, tryCount + 1);
            log.debug("Retry " + tryCount + ": " + url);
        }
    }

    private void saveFile(Response response, File toFile) throws IOException {
        if (response == null) {
            throw new RuntimeException("Response is null");
        }
        if (response.body() == null) {
            throw new RuntimeException("Http Error: " + response.code() + ", " + "empty body");
        }
        if (response.code() != 200) {
            String msg = response.body() != null ? response.body().string() : "empty body";
            throw new RuntimeException("Http Error: " + response.code() + ", " + msg);
        }

        decodeResponse(response).ifPresent(throwingConsumerWrapper(decoded -> {
            Files.write(Paths.get(toFile.getAbsolutePath()), decoded, StandardOpenOption.APPEND);
        }));
    }

    private Optional<byte[]> decodeResponse(Response response) throws IOException {
        if (response == null || response.body() == null) {
            return Optional.empty();
        }
        if (isZipped(response)) {
            return Optional.of(unzip(response.body()));
        } else {
            return Optional.of(response.body().bytes());
        }
    }

    private boolean isZipped(Response response) {
        return "gzip".equalsIgnoreCase(response.header("Content-Encoding"));
    }

    private byte[] unzip(ResponseBody body) {
        try {
            GzipSource responseBody = new GzipSource(body.source());
            return Okio.buffer(responseBody).readByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public File getDestFile() {
        return downloadInfo.getDestFile();
    }

    public DownloadUrl getDownloadUrl() {
        return downloadInfo.getDownloadUrl();
    }

    public void download() {
        try {
            DownloadUrl downloadUrl = downloadInfo.getDownloadUrl();
            setState(State.DOWNLOADING);
            M3uInfo m3uInfo = downloadM3u8()
                    .map(M3uInfo::new)
                    .orElseThrow(() -> new RuntimeException("Can't download m3u8 file"));
            createDestFile();
            List<String> contents = m3uInfo.getContents();
            for (int i = 0; i < contents.size(); i++) {
                String content = contents.get(i);
                File destFile = downloadInfo.getDestFile();
                String url = downloadUrl.getContentUrl(content);
                downloadFromUrl(url, destFile);
                observableProgress.accept(Progress.of((i + 1), contents.size()));
            }
            setState(State.COMPLETE);
        } catch (Exception e) {
            e.printStackTrace();
            setState(State.ERROR);
        }
    }

    private void setState(State state) {
        this.state = state;
        observableState.accept(state);
    }
}