package me.moonchan.streaming.downloader.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
public class MainController {

    @FXML
    public void onAddButtonClicked(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        showAddDownloadTaskDialog(stage);
    }

    public boolean showAddDownloadTaskDialog(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog_add_download_task.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // 다이얼로그 스테이지 만들기
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Download Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // 컨트롤러 설정
            AddDownloadTaskDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            String clipboardText = Clipboard.getSystemClipboard().getString();
            if(clipboardText != null)
                controller.setUrl(clipboardText);

            // 다이얼로그를 보여주고 사용자가 닫을 때까지 기다린다.
            dialogStage.showAndWait();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
