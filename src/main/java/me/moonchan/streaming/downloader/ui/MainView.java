package me.moonchan.streaming.downloader.ui;

import io.reactivex.functions.Predicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.streaming.downloader.ui.addtask.AddDownloadTaskView;
import me.moonchan.streaming.downloader.ui.download.DownloadView;
import me.moonchan.streaming.downloader.util.Constants;
import me.moonchan.streaming.downloader.util.EventBus;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Slf4j
public class MainView {

    @FXML
    AnchorPane mainPane;
    @FXML
    DownloadView downloadController;

    MainViewModel viewModel;

    @FXML
    private void initialize() {
        this.viewModel = new MainViewModel(downloadController);
        EventBus.get().getObservable(ActionEvent.class, filterAddDownloadTask)
                .subscribe(e -> showAddDownloadTaskDialog());
        EventBus.get().getObservable(ActionEvent.class, filterClearFinishedTask)
                .subscribe(e -> viewModel.clearFinishedTask());
    }

    private Predicate<ActionEvent> filterAddDownloadTask = e -> {
        String id = ((Button) e.getSource()).getId();
        return id.equals(Constants.ComponentId.BTN_ADD_DOWNLOAD_TASK);
    };

    private Predicate<ActionEvent> filterClearFinishedTask = e -> {
        String id = ((Button) e.getSource()).getId();
        return id.equals(Constants.ComponentId.BTN_CLEAR_FINISHED_TASK);
    };

    private boolean showAddDownloadTaskDialog() {
        try {
            Stage primaryStage = (Stage) mainPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/dialog_add_download_task.fxml"));

            Stage dialogStage = createDialogStage(primaryStage, loader);

            // 컨트롤러 설정
            AddDownloadTaskView addDownloadTaskView = loader.getController();
            addDownloadTaskView.setDialogStage(dialogStage);
            viewModel.initAddDownloadTaskView(addDownloadTaskView);

            // 다이얼로그를 보여주고 사용자가 닫을 때까지 기다린다.
            dialogStage.showAndWait();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Stage createDialogStage(Stage primaryStage, FXMLLoader loader) throws IOException {
        AnchorPane page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add Download Task");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        return dialogStage;
    }

}
