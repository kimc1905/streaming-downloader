package me.moonchan.ts.downloader.core.url;

import me.moonchan.ts.downloader.core.domain.url.DownloadUrl;
import me.moonchan.ts.downloader.core.exception.UrlParseException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DownloadUrlTest {

    /**
    @Test
    public void parse() throws Exception {
        String url = "https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media_1916.ts";
        DownloadUrl downloadUrl = new DownloadUrl(url);

        assertThat(downloadUrl.getEnd(), is(1916));
        assertThat(downloadUrl.getUrlFormat(), is("https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media_%d.ts"));
    }

    @Test
    public void parseWithIncludeNumber() throws Exception {
        String url = "https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media012_1916.ts";
        DownloadUrl downloadUrl = new DownloadUrl(url);

        assertThat(downloadUrl.getEnd(), is(1916));
        assertThat(downloadUrl.getUrlFormat(), is("https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media012_%d.ts"));
    }

    @Test(expected = UrlParseException.class)
    public void parseNotTs() throws Exception {
        String url = "https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media_1916.html";
        DownloadUrl downloadUrl = new DownloadUrl(url);
    }

    @Test(expected = UrlParseException.class)
    public void parseWithoutSequenceNumber() throws Exception {
        String url = "https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media.ts";
        DownloadUrl downloadUrl = new DownloadUrl(url);
    }

    @Test
    public void parseWithParameter() {
        String url = "https://test.com/test/media_1234.ts?parameter=test";
        DownloadUrl downloadUrl = new DownloadUrl(url);
        assertThat(downloadUrl.getUrlFormat(), is("https://test.com/test/media_%d.ts?parameter=test"));
    }

    @Test
    public void getUrl() throws Exception {
        String url = "https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media_1916.ts";
        DownloadUrl downloadUrl = new DownloadUrl(url);

        assertThat(downloadUrl.getUrl(1), is("https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media_1.ts"));
        assertThat(downloadUrl.getUrl(100), is("https://test.com/hls/S01/S01_10000215291.1/4/1000/1/media_100.ts"));
    }
    **/
}
