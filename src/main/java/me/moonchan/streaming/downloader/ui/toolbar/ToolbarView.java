package me.moonchan.streaming.downloader.ui.toolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.log4j.Log4j;

@Log4j
public class ToolbarView {

    ToolbarViewModel viewModel;

    @FXML
    private void initialize() {
        viewModel = new ToolbarViewModel();
    }

    @FXML
    public void onAddButtonClicked(ActionEvent e) {
        viewModel.publishClickEvent(e);
    }

    @FXML
    public void onClearButtonClicked(ActionEvent e) {
        viewModel.publishClickEvent(e);
    }
}
