package me.moonchan.streaming.downloader;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        File dest = new File("C:/Video/닥터X.S04.E07.ts");
        DownloadTask downloadTask = new DownloadTask(dest);
        String url = "https://vod-c3901.cdn.pooq.co.kr";
        Cookie cookie = new Cookie(url);
        cookie.putCookie("CloudFront-Key-Pair-Id", "APKAJ6KCI2B6BKBQMD4A");
        cookie.putCookie("CloudFront-Policy", "eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cCo6Ly8qLmNkbi5wb29xLmNvLmtyL2hscy9DMzkwMS9DMzkwMV9DMzkwMDAwMDAwNDlfMDFfMDAwNy4xLzEvKiIsIkNvbmRpdGlvbiI6eyJEYXRlTGVzc1RoYW4iOnsiQVdTOkVwb2NoVGltZSI6MTU0OTQ2MTA0MX0sIklwQWRkcmVzcyI6eyJBV1M6U291cmNlSXAiOiIyMTEuMjE1LjAuMC8xNiJ9fX1dLCJ0aWQiOiI5OTQ4Nzk2OTQ0MSIsInZlciI6IjMifQ__");
        cookie.putCookie("CloudFront-Signature", "eqI2oVeLQELprRTtZ6-6YFn4SRm89grKZID33RF0rOT6vsHN3mb-t16sP3Gd-zxfBfXVuqK%7E5j4NanDcUNwvv749oMFaJoNsAFHpHX-TT3OvFeN6P22LZjar4Ve-FE841t-D5r7XxcsQVYUmWoR1oKvc2CITg5E8xnUlDAj5Hwv6xYmeCEVfypVVogA87sLiMg8MSqD8JVA6m5YOZGyYGkBfJpOZlpcOuk4IUgeaCCltJh35DbkR4e5zx-gjKBPngfBnz2Gd6a224N6aPs3d%7Edhg4To2pBltI7rPWgoXGEBsYatWweNpC8hIZlobWQkQQLXjL8pbxt-k0AUNaq9oqw__");

        downloadTask.setCookie(cookie);

        String lastSequence = "https://vod-c3901.cdn.pooq.co.kr/hls/C3901/C3901_C39000000049_01_0007.1/1/2000/1/media_1365.ts";

        downloadTask.download(lastSequence);
    }
}
