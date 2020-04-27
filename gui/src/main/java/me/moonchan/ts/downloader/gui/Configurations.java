package me.moonchan.ts.downloader.gui;

import me.moonchan.ts.downloader.gui.util.AppPreferences;
import me.moonchan.ts.downloader.gui.util.EventBus;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class Configurations {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public AppPreferences appPreferences() {
        return new AppPreferences();
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}