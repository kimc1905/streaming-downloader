import me.moonchan.streaming.downloader.DownloadUrl;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DownloadUrlTest {

    @Test
    public void parse() throws Exception {
        String url = "https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media_1916.ts";
        DownloadUrl downloadUrl = DownloadUrl.of(url);

        assertThat(downloadUrl.getEnd(), is(1916));
        assertThat(downloadUrl.getUrlFormat(), is("https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media_%d.ts"));
    }

    @Test
    public void parse2() throws Exception {
        String url = "https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media012_1916.ts";
        DownloadUrl downloadUrl = DownloadUrl.of(url);

        assertThat(downloadUrl.getEnd(), is(1916));
        assertThat(downloadUrl.getUrlFormat(), is("https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media012_%d.ts"));
    }

    @Test
    public void parseWithBitrate() throws Exception {
        String url = "https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media_1916.ts";
//        DownloadUrl downloadUrl = DownloadUrl.of(url, 5000);
//
//        assertThat(downloadUrl.getEnd(), is(1916));
//        assertThat(downloadUrl.getUrlFormat(), is("https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/5000/1/media_%d.ts"));
    }

    @Test(expected = RuntimeException.class)
    public void parseNotTs() throws Exception {
        String url = "https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media_1916.html";
        DownloadUrl downloadUrl = DownloadUrl.of(url);
    }

    @Test(expected = RuntimeException.class)
    public void parseWithoutSequenceNumber() throws Exception {
        String url = "https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media.ts";
        DownloadUrl downloadUrl = DownloadUrl.of(url);
    }

    @Test
    public void getUrl() throws Exception {
        String url = "https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media_1916.ts";
        DownloadUrl downloadUrl = DownloadUrl.of(url);

        assertThat(downloadUrl.getUrl(1), is("https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media_1.ts"));
        assertThat(downloadUrl.getUrl(100), is("https://vod-s01.cdn.pooq.co.kr/hls/S01/S01_10000215291.1/4/1000/1/media_100.ts"));
    }
}
