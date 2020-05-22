package me.moonchan.ts.downloader.core.domain.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

public class M3uInfo {
    private Map<String, String> components;
    private BigDecimal duration;
    private List<String> contents;

    private M3uInfo() {
        components = new HashMap<>();
        contents = new ArrayList<>();
    }

    public M3uInfo(InputStream source) {
        this();
        try {
            parse(source);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't parse M3u file");
        }
    }

    public Optional<String> getComponentValue(String key) {
        String value = components.get(key);
        return value == null ? Optional.empty() : Optional.of(value);
    }

    public int getDuration() {
        return duration.toBigInteger().intValue();
    }

    public List<String> getContents() {
        return Collections.unmodifiableList(contents);
    }

    private void addComponent(String componentStr) {
        if(!componentStr.startsWith("#"))
            return;
        String[] componentArray = componentStr.split(":");
        addComponent(componentArray[0], componentArray[1]);
    }

    private void addComponent(String key, String value) {
        components.put(key, value);
    }

    private void parse(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        String line = reader.readLine();
        if(!line.equals("#EXTM3U"))
            return;
        String componentStr = "";
        while((line = reader.readLine()) != null) {
            if(line.startsWith("#EXTINF")) {
                readContents(reader, line);
            } else {
                if (line.startsWith("#")){
                    addComponent(componentStr);
                    componentStr = "";
                }
                componentStr += line;
            }
        }
        if(!componentStr.isEmpty()) {
            addComponent(componentStr);
        }
        source.close();
    }

    private void readContents(BufferedReader reader, String initStr) throws IOException {
        String contentStr = initStr;
        String line;
        while(!(line = reader.readLine()).equals("#EXT-X-ENDLIST")) {
            if(line.startsWith("#")) {
                addContent(contentStr);
                contentStr = "";
            }
            contentStr += line;
        }
        if(!contentStr.isEmpty()) {
            addContent(contentStr);
        }
    }

    public void addContent(String contentStr) {
        String[] contentArray = contentStr.split(",");
        String runtimeStr = contentArray[0].substring(contentArray[0].indexOf(":") + 1);
        double runtime = Double.parseDouble(runtimeStr);
        String content = contentArray[1].trim();

        duration.add(BigDecimal.valueOf(runtime));
        contents.add(content);
    }
}
