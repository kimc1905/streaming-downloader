package me.moonchan.streaming.downloader;

import okhttp3.OkHttpClient;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main2 {
    public static void main(String[] args) {
        int nThreads = 4;

        String[] dests = {
        };

        String[] lastUrls = {
        };

        String[] policies = {
        };

        String[] signatures = {
        };

        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for(int i = 0; i<lastUrls.length; i++) {
            String url = "https://vod-cr02.cdn.pooq.co.kr";
            Cookies cookie = new Cookies(url);

//            cookie.putCookie("CloudFront-Key-Pair-Id", "APKAJ6KCI2B6BKBQMD4A");
//            cookie.putCookie("CloudFront-Policy", policies[i]);
//            cookie.putCookie("CloudFront-Signature", signatures[i]);

//            cookie.putCookie("HEXAVID_LOGIN", "1abad159d63fabfeAalRBX7kjpp0oJn7b1WnbErXcmhW7dFFJs6Ji2n-uhhyKCaLTMj-AwcLt3CvLV-sViqEzIcAbWUmjb8VMvLinPHM_VWcAEPA33SUMbsBoaYUySCu-cHh4p4ko14odwGddwPYd0Nc8a9Cud3kFwyhardCBaQvQe6z81LDJdoLF9o9gjQYlqMGji73mqID-tCuPVIsTV1pWy8CqFWwJtrBpSB4qN0W2MO7yr_e8x-8GqIxW8-SEm5S-VSswTd3dORm5JRXgqgo5wACX8m_nH8wCW9tABdYw0VuLI8DkyS0BInW4pGrU0kvI0H4SUBq9bqt");
//            cookie.putCookie("hexatrade","i6o2nquyew38WKfKQ53apSPIl8gK2vH0");

            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            final String lastUrl = lastUrls[i];
//            downloadTask.setUrlFormat(lastUrl);
//            downloadTask.setUrlFormatWithBitrate(lastUrl, 5000);
            DownloadUrl downloadUrl = DownloadUrl.of(lastUrl, 5000);
            DownloadTask downloadTask = new DownloadTask(client, dests[i], downloadUrl, cookie);
            Thread run = new Thread(() -> {
                downloadTask.run();
            });
            executorService.submit(run);
        }
    }
}
