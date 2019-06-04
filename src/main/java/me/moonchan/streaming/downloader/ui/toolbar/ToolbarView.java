package me.moonchan.streaming.downloader.ui.toolbar;

import com.jakewharton.rxrelay2.Relay;
import javafx.fxml.FXML;

public class ToolbarView {

    ToolbarViewModel viewModel;

    @FXML
    private void initialize() {
        viewModel = new ToolbarViewModel();
    }

    @FXML
    public void onAddButtonClicked() {
        viewModel.publishAddEvent();
    }

    @FXML
    public void onClearButtonClicked() {
        viewModel.publishClearEvent();
    }

    public Relay<String> getRelayToolbarEvent() {
        return viewModel.getRelayToolbarEvent();
    }
}
