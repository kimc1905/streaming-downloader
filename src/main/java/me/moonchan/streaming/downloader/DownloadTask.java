package me.moonchan.streaming.downloader;

import lombok.extern.log4j.Log4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Log4j
public class DownloadTask implements Runnable {

    public enum DownloadState {
        READY, DOWNLOADING, PAUSE, ERROR, COMPLETE
    }

    private File dest;
    private Cookies cookie;
    private OkHttpClient client;
    private DownloadUrl downloadUrl;
    private OnDownloadListener onDownloadListener;
    private OnDownloadStateChangeListener onDownloadStateChangeListener;

    public DownloadTask(OkHttpClient client, String dest, DownloadUrl downloadUrl) {
        this(client, dest, downloadUrl, null);
    }

    public DownloadTask(OkHttpClient client, String dest, DownloadUrl downloadUrl, Cookies cookies) {
        this.client = client;
        this.dest = new File(dest);
        this.downloadUrl = downloadUrl;
        this.cookie = cookies;
    }

    public void setCookie(Cookies cookie) {
        this.cookie = cookie;
    }

    public void setDownloadUrl(DownloadUrl downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setOnDownloadListener(OnDownloadListener downloadListener) {
        this.onDownloadListener = downloadListener;
    }

    public void setOnDownloadStateChangeListener(OnDownloadStateChangeListener downloadStateChangeListener) {
        this.onDownloadStateChangeListener = downloadStateChangeListener;
    }

    private void createDestFile() throws IOException{
        if (dest.exists())
            dest.delete();
        dest.createNewFile();
    }

    private void downloadFromUrl(String url) throws IOException {
        log.debug("Request: " + url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(cookie != null)
            requestBuilder.addHeader("Cookies", cookie.toString());
        Request request = requestBuilder
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.63.11 Safari/537.36")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept", "*/*")
                .build();
        Response response = client.newCall(request).execute();

        saveFile(response);
    }

    private void saveFile(Response response) throws IOException{
        if(response == null || response.code() != 200) {
            throw new RuntimeException("Http Error: " + response.code() + ", " + response.body().string());
        }
        Files.write(Paths.get(dest.getAbsolutePath()), response.body().bytes(), StandardOpenOption.APPEND);
    }

    public File getDest() {
        return dest;
    }

    public DownloadUrl getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public void run() {
        try {
            createDestFile();
            onDownloadStateChange(DownloadState.DOWNLOADING);
            int start = downloadUrl.getStart();
            int end = downloadUrl.getEnd();
            int length = end - start + 1;
            for (int i = start; i <= end; i++) {
                downloadFromUrl(downloadUrl.getUrl(i));
                onDownload((i - start + 1), length);
            }
        }catch (IOException e) {
            e.printStackTrace();
            onDownloadStateChange(DownloadState.ERROR);
        }
        onDownloadStateChange(DownloadState.COMPLETE);
    }

    private void onDownload(int current, int length) {
        if(this.onDownloadListener != null)
            this.onDownloadListener.onDownload(current, length);
    }

    private void onDownloadStateChange(DownloadState state) {
        if(this.onDownloadStateChangeListener != null)
            this.onDownloadStateChangeListener.onDownloadStateChange(state);
    }

    public interface OnDownloadListener {
        void onDownload(int current, int length);
    }

    public interface  OnDownloadStateChangeListener {
        void onDownloadStateChange(DownloadState state);
    }
}
