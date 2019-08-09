package me.moonchan.streaming.downloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.moonchan.streaming.downloader.ui.main.MainContract;
import me.moonchan.streaming.downloader.ui.main.MainView;
import me.moonchan.streaming.downloader.util.Constants;
import me.moonchan.streaming.downloader.util.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.prefs.Preferences;

@SpringBootApplication
public class App extends Application {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private Stage mainStage;
    private MainView mainView;
    private Preferences preferences;

    @Override
    public void init() throws Exception {
        super.init();
        preferences = Preferences.userNodeForPackage(this.getClass());
        springContext = SpringApplication.run(App.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scene/main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
        mainView = fxmlLoader.getController();
    }

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        stage.setScene(new Scene(rootNode));
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        setPositionAndSize(stage);
        stage.setTitle("Streaming Downloader");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        savePositionAndSize();
        mainView.onStopApplication();
        springContext.close();
    }

    private void setPositionAndSize(Stage stage) {
        double x = preferences.getDouble(Constants.PreferenceKey.PREF_MAIN_STAGE_X, stage.getX());
        double y = preferences.getDouble(Constants.PreferenceKey.PREF_MAIN_STAGE_Y, stage.getY());
        int width = preferences.getInt(Constants.PreferenceKey.PREF_MAIN_STAGE_WIDTH, WIDTH);
        int height = preferences.getInt(Constants.PreferenceKey.PREF_MAIN_STAGE_HEIGHT, HEIGHT);
        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private void savePositionAndSize() {
        System.out.println(String.format("width: %f, height: %f", mainStage.getWidth(), mainStage.getHeight()));
        preferences.putDouble(Constants.PreferenceKey.PREF_MAIN_STAGE_WIDTH, mainStage.getWidth());
        preferences.putDouble(Constants.PreferenceKey.PREF_MAIN_STAGE_HEIGHT, mainStage.getHeight());
        preferences.putDouble(Constants.PreferenceKey.PREF_MAIN_STAGE_X, mainStage.getX());
        preferences.putDouble(Constants.PreferenceKey.PREF_MAIN_STAGE_Y, mainStage.getY());
    }

    public static void main(String[] args) {
        launch(args);
//        LauncherImpl.launchApplication(App.class, Intro.class, args);
    }
}
