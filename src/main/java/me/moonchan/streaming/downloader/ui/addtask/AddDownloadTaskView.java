package me.moonchan.streaming.downloader.ui.addtask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.log4j.Log4j;
import me.moonchan.streaming.downloader.Cookie;

@Log4j
public class AddDownloadTaskView {

    @FXML
    private TextField editUrl;
    @FXML
    private TextField editUrlFormat;
    @FXML
    private TextField editStart;
    @FXML
    private TextField editEnd;
    @FXML
    private TextField editSaveLocation;
    @FXML
    private TableView<CookieViewModel> tableCookie;
    @FXML
    private TableColumn<CookieViewModel, String> colKey;
    @FXML
    private TableColumn<CookieViewModel, String> colValue;
    @FXML
    private TextField editCookieKey;
    @FXML
    private TextField editCookieValue;

    private Stage dialogStage;
    private AddDownloadTaskViewModel viewModel;


    @FXML
    private void initialize() {
        viewModel = new AddDownloadTaskViewModel();
        bindProperty();
        initTextAreaFilter();
        initTableView();
    }

    private void bindProperty() {
        editUrl.textProperty().bindBidirectional(viewModel.getUrl());
        editUrlFormat.textProperty().bindBidirectional(viewModel.getUrlFormat());
        editStart.textProperty().bindBidirectional(viewModel.getStart());
        editEnd.textProperty().bindBidirectional(viewModel.getEnd());
        editSaveLocation.textProperty().bindBidirectional(viewModel.getSaveLocation());
        editCookieKey.textProperty().bindBidirectional(viewModel.getCookieKey());
        editCookieValue.textProperty().bindBidirectional(viewModel.getCookieValue());
    }

    private void initTextAreaFilter() {
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

    private void initTableView() {
        tableCookie.setItems(viewModel.getCookieData());
        colKey.setCellValueFactory(cellData -> cellData.getValue().getKey());
        colValue.setCellValueFactory(cellData -> cellData.getValue().getValue());
        tableCookie.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        editCookieKey.setText(newSelection.getKey().getValue());
                        editCookieValue.setText(newSelection.getValue().getValue());
                    }
                }
        );
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUrl(String text) {
        setDownloadUrl(text);
        if (!editUrlFormat.getText().isEmpty())
            editUrl.setText(text);
    }

    public void setRecentSaveFile(String path) {
        viewModel.setRecentSaveFile(path);
    }

    private void setDownloadUrl(String url) {
        viewModel.setDownloadUrl(url);
    }

    public void setCookie(Cookie cookie) {
        viewModel.setCookie(cookie);
    }

    @FXML
    private void onAutoCompleteButtonClicked() {
        viewModel.setDownloadUrl(viewModel.getUrl().get());
    }

    @FXML
    private void onBrowseButtonClicked(ActionEvent event) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        viewModel.browseSaveLocation(window);
    }

    @FXML
    private void onAddCookieButtonClicked() {
        viewModel.addCookie();
    }

    @FXML
    private void onRemoveCookieButtonClicked() {
        viewModel.removeCookie();
    }

    @FXML
    private void onCancelButtonClicked() {
        dialogStage.close();
    }

    @FXML
    private void onDoneButtonClicked() {
        try {
            viewModel.addDownloadTask();
            dialogStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
