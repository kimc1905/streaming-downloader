package me.moonchan.streaming.downloader.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import lombok.extern.log4j.Log4j;
import me.moonchan.streaming.downloader.DownloadTask;
import me.moonchan.streaming.downloader.DownloadUrl;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

@Log4j
public class DownloadController {

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

    ObservableList<DownloadTaskViewModel> data;
    private OkHttpClient client;

    @FXML
    public void initialize() {
//        loadFXML();
        data = FXCollections.observableArrayList();
        tableDownloadTask.setItems(data);
        client = new OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        addDummyData();

        log.debug(tableDownloadTask.toString());

        colName.setCellValueFactory(cellData -> cellData.getValue().getName());
        colUrl.setCellValueFactory(cellData -> cellData.getValue().getUrl());
        colFile.setCellValueFactory(cellData -> cellData.getValue().getDest());
        colState.setCellValueFactory(cellData -> cellData.getValue().getState());
        colProgress.setCellFactory(ProgressBarTableCell.forTableColumn());
        colProgress.setCellValueFactory(cellData -> cellData.getValue().getProgress().asObject());
    }


    private void addDummyData() {
        data.addAll(
                new DownloadTaskViewModel(new DownloadTask(client, "E:/공유폴더/Test01.ts", DownloadUrl.of("https://live-s16.cdn.pooq.co.kr/hls/S16/1/1000/live_779439492.ts")))
        );
    }


}
