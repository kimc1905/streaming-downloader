package me.moonchan.streaming.downloader.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import me.moonchan.streaming.downloader.DownloadUrl;

import java.io.File;
import java.util.Optional;

@Log4j
public class AddDownloadTaskDialogController {

    @FXML
    private TextField editUrl;
    @FXML
    private TextField editUrlFormat;
    @FXML
    private TextField editStart;
    @FXML
    private TextField editEnd;
    @FXML
    private TextField editDest;

    private Stage dialogStage;

    @FXML
    private void initialize() {
        editStart.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                editStart.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        editEnd.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                editEnd.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private Optional<DownloadUrl> toDownloadUrl(String url) {
        if (!(url.startsWith("http://") || url.startsWith("https://")))
            return Optional.empty();
        try {
            return Optional.of(DownloadUrl.of(url));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUrl(String text) {
        setDownloadUrl(text);
        if(!editUrlFormat.getText().isEmpty())
            editUrl.setText(text);
    }

    private void setDownloadUrl(String url) {
        log.debug(url);
        Optional<DownloadUrl> downloadUrl = toDownloadUrl(url);
        downloadUrl.map(DownloadUrl::getUrlFormat).ifPresent(editUrlFormat::setText);
        downloadUrl.map(DownloadUrl::getStart).map(String::valueOf).ifPresent(editStart::setText);
        downloadUrl.map(DownloadUrl::getEnd).map(String::valueOf).ifPresent(editEnd::setText);
    }

    @FXML
    private void onAutoCompleteButtonClicked() {
        String url = editUrl.getText();
        setDownloadUrl(url);
    }

    @FXML
    private void onBrowseButtonClicked(ActionEvent event) {
        File selectedFile = new File(editDest.getText());
        log.debug("selected file: " + selectedFile.getAbsolutePath());
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TS File", "*.ts")
        );
        if(selectedFile.isFile())
            fileChooser.setInitialDirectory(selectedFile.getParentFile());
        Node node = (Node) event.getSource();
        File saveFile = fileChooser.showSaveDialog(node.getScene().getWindow());
        if(saveFile != null)
            editDest.setText(saveFile.getAbsolutePath());
    }

    @FXML
    private void onCancelButtonClicked() {
        dialogStage.close();
    }

    @FXML
    private void onDoneButtonClicked() {

    }
}
