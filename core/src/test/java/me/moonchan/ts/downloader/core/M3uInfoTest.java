package me.moonchan.ts.downloader.core;

import me.moonchan.ts.downloader.core.domain.model.M3uInfo;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class M3uInfoTest {

    @Test
    public void parseTest1() {
        InputStream tvingM3u8 = getClass().getResourceAsStream("/tving.m3u8");
        M3uInfo m3uInfo = new M3uInfo(tvingM3u8);
        assertEquals(241, m3uInfo.getContents().size());
        assertEquals("3", m3uInfo.getComponentValue("#EXT-X-VERSION")
                .orElseThrow(() -> new RuntimeException("")));
    }

    @Test
    public void parseTest2() {
        InputStream wavveM3u8 = getClass().getResourceAsStream("/wavve.m3u8");
        M3uInfo m3uInfo = new M3uInfo(wavveM3u8);
        assertEquals(734, m3uInfo.getContents().size());
        assertEquals("3", m3uInfo.getComponentValue("#EXT-X-VERSION")
                .orElseThrow(() -> new RuntimeException("")));
    }
}