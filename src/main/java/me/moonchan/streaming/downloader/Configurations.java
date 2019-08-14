package me.moonchan.streaming.downloader;

import me.moonchan.streaming.downloader.util.EventBus;
import me.moonchan.streaming.downloader.util.JsonPreferences;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {

    @Bean
    public JsonPreferences jsonPreferences() {
        return new JsonPreferences();
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}