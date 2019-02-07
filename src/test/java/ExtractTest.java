import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractTest {

    @Test
    public void test() {
        String format = "https://vod-cr02.cdn.pooq.co.kr/hls/CR02/CR02_DN0000000183_01_0001.1/1/2000/1/media_1752.ts";

        int beginIndex = format.lastIndexOf("/");
        String fileName = format.substring(beginIndex + 1);
        System.out.println(fileName);

        Pattern pattern = Pattern.compile("(\\D+)(\\d+)(\\D+)");
        Matcher matcher = pattern.matcher(fileName);

        if(matcher.find()) {
            String fo = new StringBuilder()
                    .append(format.substring(0, beginIndex))
                    .append("/" + matcher.group(1))
                    .append("%d")
                    .append(matcher.group(3))
                    .toString();
            System.out.println(fo);
        }
    }
}
