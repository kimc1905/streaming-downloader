package me.moonchan.ts.downloader.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.gui.ui.main.MainView;
import me.moonchan.ts.downloader.gui.util.AppPreferences;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class App extends Application {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private Stage mainStage;
    private MainView mainView;
    private AppPreferences preferences;

    @Override
    public void init() throws Exception {
        super.init();
        preferences = new AppPreferences();
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
        double x = preferences.getMainStageX(stage.getX());
        double y = preferences.getMainStageY(stage.getY());
        double width = preferences.getMainStageWidth(WIDTH);
        double height = preferences.getMainStageHeight(HEIGHT);
        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private void savePositionAndSize() {
        log.debug(String.format("width: %f, height: %f", mainStage.getWidth(), mainStage.getHeight()));
        preferences.setMainStageWidth(mainStage.getWidth());
        preferences.setMainStageHeight(mainStage.getHeight());
        preferences.setMainStageX(mainStage.getX());
        preferences.setMainStageY(mainStage.getY());
    }

    public static void main(String[] args) {
        launch(args);
//        LauncherImpl.launchApplication(App.class, Intro.class, args);
    }
}