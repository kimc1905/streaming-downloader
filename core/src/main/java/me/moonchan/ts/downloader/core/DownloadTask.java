package me.moonchan.ts.downloader.core;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.core.url.DownloadUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.GzipSource;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

    private void downloadFromUrl(String url, File dest) throws IOException {
        downloadFromUrl(url, dest, 1);
    }

    private void downloadFromUrl(String url, File dest, int tryCount) throws IOException {
        try {
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
            Response response = client.newCall(request).execute();
            saveFile(response, dest);
        } catch (SocketException e) {
            if (tryCount > RETRY) {
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
        if (response.code() != 200) {
            String msg = response.body() != null ? response.body().string() : "empty body";
            throw new RuntimeException("Http Error: " + response.code() + ", " + msg);
        }
        if(response.body() == null) {
            throw new RuntimeException("Http Error: " + response.code() + ", " + "empty body");
        }

        Files.write(Paths.get(toFile.getAbsolutePath()), decodeResponse(response), StandardOpenOption.APPEND);
    }

    private byte[] decodeResponse(Response response) throws IOException {
        if(isZipped(response)) {
            return unzip(response.body());
        } else {
            return response.body().bytes();
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
            int start = downloadUrl.getStart();
            int end = downloadUrl.getEnd();
            int length = end - start + 1;
            log.debug("-> Download Start: " + downloadUrl.getUrl(start));
            log.debug("   length: " + length);

            createDestFile();
            for (int i = start; i <= end; i++) {
                File destFile = downloadInfo.getDestFile();
                downloadFromUrl(downloadUrl.getUrl(i), destFile);
                observableProgress.accept(Progress.of((i - start + 1), length));
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