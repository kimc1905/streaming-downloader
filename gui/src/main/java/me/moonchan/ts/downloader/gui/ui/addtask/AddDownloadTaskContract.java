package me.moonchan.ts.downloader.gui.ui.addtask;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.moonchan.ts.downloader.core.domain.model.Bitrate;
import me.moonchan.ts.downloader.gui.ui.BaseContract;

import java.io.File;
import java.util.Optional;

public class AddDownloadTaskContract {
    public interface View extends BaseContract.View {
        void setDialogStage(Stage dialogStage);
        void showBitrateBox(boolean show);
        void setSelectBitrate(Bitrate bitrate);
        Scene getScene();
    }
    interface Presenter extends BaseContract.Presenter<View> {
        void init();
        void bindUrl(TextField tfUrl);
        void bindUrlFormat(TextField tfUrlFormat);
        void bindSaveLocation(TextField tfSaveLocation);
        void bindCookieKey(TextField tfCookieKey);
        void bindCookieValue(TextField tfCookieValue);
        void bindCookieTableView(TableView<CookieViewModel> tableCookie);
        Optional<File> getRecentSaveDir();
        void setSaveLocation(File saveFile);
        void changeBitrate(Bitrate bitrate);
        void addCookie();
        void removeCookie();
        void addDownloadTask();
        void autoComplete();
        void setM3u8Url(String m3u8Url);
    }
}