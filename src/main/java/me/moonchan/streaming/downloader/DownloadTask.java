package me.moonchan.streaming.downloader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.subscriptions.CompositeSubscription;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DownloadTask implements Runnable {

    private File dest;
    private Cookies cookie;
    private OkHttpClient client;
    private DownloadUrl downloadUrl;

    public DownloadTask(File dest, OkHttpClient client) {
        this.dest = dest;
        this.client = client;
    }

    public DownloadTask(File dest, OkHttpClient client, Cookies cookie) {
        this(dest, client);
        this.cookie = cookie;
    }

    public void setCookie(Cookies cookie) {
        this.cookie = cookie;
    }

    public void setDownloadUrl(DownloadUrl downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    private void createDestFile() throws IOException{
        if (dest.exists())
            dest.delete();
        dest.createNewFile();
    }

    private void downloadFromUrl(String url) throws IOException {
        System.out.println("Request: " + url);
        Request request = new Request.Builder().url(url)
                .addHeader("Cookies", cookie.toString())
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

    @Override
    public void run() {
        try {
            createDestFile();
            for (int i = downloadUrl.getStart(); i <= downloadUrl.getEnd(); i++) {
                downloadFromUrl(downloadUrl.getUrl(i));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
