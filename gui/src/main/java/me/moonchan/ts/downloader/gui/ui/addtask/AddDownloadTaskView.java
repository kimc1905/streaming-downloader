package me.moonchan.ts.downloader.gui.ui.addtask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.core.Bitrate;
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
    @FXML
    private HBox boxBitrate;
    @FXML
    private RadioButton rbtMd;
    @FXML
    private RadioButton rbtSd;
    @FXML
    private RadioButton rbtHd;
    @FXML
    private RadioButton rbtFhd;

    private ToggleGroup toggleGroup;
    private Stage dialogStage;
    private AddDownloadTaskContract.Presenter presenter;

    @Autowired
    public AddDownloadTaskView(AddDownloadTaskContract.Presenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
        this.toggleGroup = new ToggleGroup();
    }

    @FXML
    private void initialize() {
        log.debug("initialize AddDownloadTaskView");
        bindProperty();
        initTextAreaFilter();
        initTableView();
        initBitrateView();
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
        editUrlFormat.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) presenter.setUrlFormat(newValue);
        });
        editStart.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                editStart.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (!editStart.getText().isEmpty()) {
                presenter.setStart(Integer.parseInt(editStart.getText()));
            }
        });
        editEnd.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                editEnd.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (!editEnd.getText().isEmpty()) {
                presenter.setEnd(Integer.parseInt(editEnd.getText()));
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

    private void initBitrateView() {
        rbtMd.setToggleGroup(toggleGroup);
        rbtSd.setToggleGroup(toggleGroup);
        rbtHd.setToggleGroup(toggleGroup);
        rbtFhd.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            Bitrate bitrate = Bitrate.valueOf((String)(toggleGroup.getSelectedToggle().getUserData()));
            presenter.changeBitrate(bitrate);
        });
    }

    public void showBitrateBox(boolean show) {
        boxBitrate.setVisible(show);
        double height = show ? 40 : 0;
        boxBitrate.setMaxHeight(height);
        boxBitrate.setPrefHeight(height);
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

    public void setSelectBitrate(Bitrate bitrate) {
        switch (bitrate) {
            case MOBILE:
                rbtMd.setSelected(true);
                break;
            case SD:
                rbtSd.setSelected(true);
                break;
            case HD:
                rbtHd.setSelected(true);
                break;
            case FHD:
                rbtFhd.setSelected(true);
                break;
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