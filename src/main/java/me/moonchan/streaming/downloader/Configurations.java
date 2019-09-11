package me.moonchan.streaming.downloader;

import me.moonchan.streaming.downloader.util.AppPreferences;
import me.moonchan.streaming.downloader.util.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {

    @Bean
    public AppPreferences appPreferences() {
        return new AppPreferences();
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}