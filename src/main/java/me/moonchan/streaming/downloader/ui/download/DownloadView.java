package me.moonchan.streaming.downloader.ui.download;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DownloadView implements DownloadContract.View {
    @FXML
    private TableView<DownloadTaskViewModel> tableDownloadTask;
    @FXML
    private TableColumn<DownloadTaskViewModel, String> colName;
    @FXML
    private TableColumn<DownloadTaskViewModel, String> colUrl;
    @FXML
    private TableColumn<DownloadTaskViewModel, String> colFile;
    @FXML
    private TableColumn<DownloadTaskViewModel, String> colState;
    @FXML
    private TableColumn<DownloadTaskViewModel, Double> colProgress;

    private DownloadContract.Presenter presenter;

    @Autowired
    public DownloadView(DownloadContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @FXML
    private void initialize() {
        presenter.bindDownloadTaskTable(tableDownloadTask);
        colName.setCellValueFactory(cellData -> cellData.getValue().getName());
        colUrl.setCellValueFactory(cellData -> cellData.getValue().getUrl());
        colFile.setCellValueFactory(cellData -> cellData.getValue().getDest());
        colState.setCellValueFactory(cellData -> cellData.getValue().getState());
        colProgress.setCellFactory(ProgressBarTableCell.forTableColumn());
        colProgress.setCellValueFactory(cellData -> cellData.getValue().getProgress().asObject());
    }

//    public void addDownloadTask(DownloadTask task) {
//        presenter.addDownloadTask(task);
//    }
//
//    public void clearFinishedDownloadTask() {
//        presenter.clearFinishedDownloadTask();
//    }

}
