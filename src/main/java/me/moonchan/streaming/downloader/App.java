package me.moonchan.streaming.downloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.moonchan.streaming.downloader.util.Constants;
import me.moonchan.streaming.downloader.util.EventBus;

import java.io.IOException;

public class App extends Application {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Streaming Downloader");
        this.initRootLayout();
        primaryStage.show();
    }

    private void initRootLayout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/scene/main.fxml"));
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        EventBus.get().post(Constants.EventMessage.APPLICATION_STOP);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
