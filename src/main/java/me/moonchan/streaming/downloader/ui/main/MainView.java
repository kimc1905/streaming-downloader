package me.moonchan.streaming.downloader.ui.main;

import io.reactivex.functions.Predicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.streaming.downloader.ui.addtask.AddDownloadTaskContract;
import me.moonchan.streaming.downloader.ui.addtask.AddDownloadTaskView;
import me.moonchan.streaming.downloader.ui.download.DownloadView;
import me.moonchan.streaming.downloader.util.Constants;
import me.moonchan.streaming.downloader.util.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Slf4j
public class MainView implements MainContract.View {

    @FXML
    private AnchorPane mainPane;
    private MainContract.Presenter presenter;
    private AddDownloadTaskContract.View addDownloadTaskView;
    private ApplicationContext appContext;

    @Autowired
    public MainView(ApplicationContext context, MainContract.Presenter presenter, AddDownloadTaskContract.View addDownloadTaskView) {
        this.appContext = context;
        this.presenter = presenter;
        this.addDownloadTaskView = addDownloadTaskView;
    }

    @FXML
    private void initialize() {
        presenter.setView(this);
    }

    @Override
    public void showAddDownloadTaskDialog() {
        try {
            Stage primaryStage = (Stage) mainPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/dialog_add_download_task.fxml"));
            Stage dialogStage = createDialogStage(primaryStage, loader);
            // 컨트롤러 설정
            addDownloadTaskView.setDialogStage(dialogStage);
            presenter.initAddDownloadTaskView(addDownloadTaskView);
            // 다이얼로그를 보여주고 사용자가 닫을 때까지 기다린다.
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage createDialogStage(Stage primaryStage, FXMLLoader loader) throws IOException {
        loader.setControllerFactory(appContext::getBean);
        loader.getController();
        AnchorPane page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add Download Task");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.setX(getMidX(primaryStage));
        dialogStage.setY(getMidY(primaryStage));
        return dialogStage;
    }

    private double getMidX(Stage parent) {
        return parent.getX() + (parent.getWidth() / 2.0) - 350;
    }

    private double getMidY(Stage parent) {
        return parent.getY() + (parent.getHeight() / 2.0) - 225;
    }

    public void onStopApplication() {
        presenter.shutdownDownloader();
    }
}
