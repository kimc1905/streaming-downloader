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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadTask {

    private File dest;
    private Cookie cookie;
    private OkHttpClient client;
    private CompositeSubscription subscription;

    public DownloadTask(File dest) {
        this.dest = dest;
        client = new OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
        subscription = new CompositeSubscription();
    }


    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public void download(String lastSeqUrl) {
        int beginIndex = lastSeqUrl.lastIndexOf("/");
        String fileName = lastSeqUrl.substring(beginIndex + 1);
        System.out.println(fileName);

        Pattern pattern = Pattern.compile("(\\D+)(\\d+)(\\D+)");
        Matcher matcher = pattern.matcher(fileName);

        if(matcher.find()) {
            String format = new StringBuilder()
                    .append(lastSeqUrl.substring(0, beginIndex))
                    .append("/" + matcher.group(1))
                    .append("%d")
                    .append(matcher.group(3))
                    .toString();
            download(format, 1, Integer.parseInt(matcher.group(2)));
        }
    }

    public void download(String format, int start, int end) {
       try {
            if (!dest.exists())
                dest.createNewFile();
            for (int i = start; i <= end; i++) {
                String url = String.format(format, i);
                downloadFromUrl(url);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadFromUrl(String url) throws IOException {
        System.out.println("Request: " + url);
//                System.out.println(cookie.toString());
        Request request = new Request.Builder().url(url)
                .addHeader("Cookie", cookie.toString())
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.63.11 Safari/537.36")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept", "*/*")
                .build();
        Response response = client.newCall(request).execute();

        saveFile(url, response);
    }

    private void saveFile(String url, Response response) throws IOException{
        if(response == null || response.code() != 200) {
            System.out.println("Error: " + response.code() + ", " + url);
            return;
        }
        Files.write(Paths.get(dest.getAbsolutePath()), response.body().bytes(), StandardOpenOption.APPEND);
    }
}
