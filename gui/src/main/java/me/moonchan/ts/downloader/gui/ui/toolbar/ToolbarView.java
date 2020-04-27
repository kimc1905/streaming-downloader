package me.moonchan.ts.downloader.gui.ui.toolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ToolbarView implements ToolbarContract.View {
    private ToolbarContract.Presenter presenter;

    public ToolbarView(ToolbarContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @FXML
    private void initialize() {
        presenter.setView(this);
    }

    @FXML
    public void onAddButtonClicked(ActionEvent e) {
        presenter.sendEventMessage(e);
    }

    @FXML
    public void onClearButtonClicked(ActionEvent e) {
        presenter.sendEventMessage(e);
    }
}