package me.moonchan.streaming.downloader;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class DownloadTask implements Runnable {

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
            return (double)current / (double)length;
        }
    }

    private static final int RETRY = 3;
    public static final State INIT_STATE = State.READY;

    private File saveLocation;
    private Cookie cookie;
    private OkHttpClient client;
    private DownloadUrl downloadUrl;
    private State state;
    @Getter
    private final Relay<State> observableState = PublishRelay.create();
    @Getter
    private final Relay<Progress> observableProgress = PublishRelay.create();

    public DownloadTask(OkHttpClient client, File saveLocation, DownloadUrl downloadUrl) {
        this(client, saveLocation, downloadUrl, null);
    }

    public DownloadTask(OkHttpClient client, File saveLocation, DownloadUrl downloadUrl, Cookie cookie) {
        this.client = client;
        this.saveLocation = saveLocation;
        this.downloadUrl = downloadUrl;
        this.cookie = cookie;
        this.state = INIT_STATE;
    }

    public DownloadTask(OkHttpClient client, DownloadInfo downloadInfo) {
        this.client = client;
        this.saveLocation = downloadInfo.getSaveLocation();
        this.downloadUrl = downloadInfo.getDownloadUrl();
        this.cookie = downloadInfo.getCookie();
        this.state = INIT_STATE;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public void setDownloadUrl(DownloadUrl downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isFinished() {
        return (state == State.COMPLETE || state == State.ERROR);
    }

    private void createDestFile() throws IOException {
        if (saveLocation.exists())
            saveLocation.delete();
        saveLocation.createNewFile();
    }

    private void downloadFromUrl(String url, int tryCount) throws IOException {
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url);
            if (cookie != null)
                requestBuilder.addHeader("Cookie", cookie.toString());
            Request request = requestBuilder
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.63.11 Safari/537.36")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .addHeader("Accept", "*/*")
                    .build();
            Response response = client.newCall(request).execute();
            saveFile(response);
        } catch (SocketException e) {
            if (tryCount > RETRY) {
                throw e;
            }
            downloadFromUrl(url, tryCount + 1);
            log.debug("Retry " + tryCount + ": " + url);
        }
    }

    private void saveFile(Response response) throws IOException {
        if (response == null || response.code() != 200) {
            throw new RuntimeException("Http Error: " + response.code() + ", " + response.body().string());
        }
        Files.write(Paths.get(saveLocation.getAbsolutePath()), response.body().bytes(), StandardOpenOption.APPEND);
    }

    public File getSaveLocation() {
        return saveLocation;
    }

    public DownloadUrl getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public void run() {
        try {
            createDestFile();
            observableState.accept(State.DOWNLOADING);
            int start = downloadUrl.getStart();
            int end = downloadUrl.getEnd();
            int length = end - start + 1;
            log.debug("-> Download Start: " + downloadUrl.getUrlFormat());
            log.debug("   length: " + length);
            for (int i = start; i <= end; i++) {
                downloadFromUrl(downloadUrl.getUrl(i), 1);
                observableProgress.accept(Progress.of((i - start + 1), length));
            }
            observableState.accept(State.COMPLETE);
        } catch (IOException e) {
            e.printStackTrace();
            observableState.accept(State.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            observableState.accept(State.ERROR);
        }
    }

}
