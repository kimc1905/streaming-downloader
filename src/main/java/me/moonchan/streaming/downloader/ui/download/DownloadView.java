package me.moonchan.streaming.downloader.ui.download;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import me.moonchan.streaming.downloader.DownloadTask;

public class DownloadView {
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

    private DownloadViewModel viewModel;

    @FXML
    private void initialize() {
        viewModel = new DownloadViewModel();
        tableDownloadTask.setItems(viewModel.getDownloadTasks());
        colName.setCellValueFactory(cellData -> cellData.getValue().getName());
        colUrl.setCellValueFactory(cellData -> cellData.getValue().getUrl());
        colFile.setCellValueFactory(cellData -> cellData.getValue().getDest());
        colState.setCellValueFactory(cellData -> cellData.getValue().getState());
        colProgress.setCellFactory(ProgressBarTableCell.forTableColumn());
        colProgress.setCellValueFactory(cellData -> cellData.getValue().getProgress().asObject());
    }

    public void addDownloadTask(DownloadTask task) {
        viewModel.addDownloadTask(task);
    }

    public void clearFinishedDownloadTask() {
        viewModel.clearFinishedDownloadTask();
    }

}
