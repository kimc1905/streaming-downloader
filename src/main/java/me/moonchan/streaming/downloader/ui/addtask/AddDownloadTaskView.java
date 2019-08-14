package me.moonchan.streaming.downloader.ui.addtask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
@Slf4j
public class AddDownloadTaskView implements AddDownloadTaskContract.View {
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
    private AddDownloadTaskContract.Presenter presenter;

    @Autowired
    public AddDownloadTaskView(AddDownloadTaskContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @FXML
    private void initialize() {
        log.debug("initialize AddDownloadTaskView");
        bindProperty();
        initTextAreaFilter();
        initTableView();
        presenter.init();
    }

    private void bindProperty() {
        presenter.bindUrl(editUrl);
        presenter.bindUrlFormat(editUrlFormat);
        presenter.bindStart(editStart);
        presenter.bindEnd(editEnd);
        presenter.bindSaveLocation(editSaveLocation);
        presenter.bindCookieKey(editCookieKey);
        presenter.bindCookieValue(editCookieValue);
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
        presenter.bindCookieTableView(tableCookie);
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

    @Override
    public Scene getScene() {
        return tableCookie.getScene();
    }

    @FXML
    private void onAutoCompleteButtonClicked() {
        presenter.autoComplete();
    }

    @FXML
    private void onBrowseButtonClicked(ActionEvent event) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        browseSaveLocation(window);
    }

    @FXML
    private void onAddCookieButtonClicked() {
        presenter.addCookie();
    }

    @FXML
    private void onRemoveCookieButtonClicked() {
        presenter.removeCookie();
    }

    @FXML
    private void onCancelButtonClicked() {
        dialogStage.close();
    }

    @FXML
    private void onDoneButtonClicked() {
        try {
            presenter.addDownloadTask();
            dialogStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void browseSaveLocation(Window window) {
        FileChooser fileChooser;
        fileChooser = createTsFileChooser();
        presenter.getRecentSaveDir().ifPresent(fileChooser::setInitialDirectory);
        File saveFile = fileChooser.showSaveDialog(window);
        if (saveFile != null) {
            presenter.setSaveLocation(saveFile);
        }
    }

    private FileChooser createTsFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TS File", "*.ts")
        );
        return fileChooser;
    }
}