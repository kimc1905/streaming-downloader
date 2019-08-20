package me.moonchan.streaming.downloader.ui.addtask;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.moonchan.streaming.downloader.ui.BaseContract;

import java.io.File;
import java.util.Optional;

public class AddDownloadTaskContract {
    public interface View extends BaseContract.View {
        void setDialogStage(Stage dialogStage);
        void showBitrateBox(boolean show);
        void setSelectBitrate(int bitrate);
        Scene getScene();
    }
    interface Presenter extends BaseContract.Presenter<View> {
        void init();
        void bindUrl(TextField tfUrl);
        void bindUrlFormat(TextField tfUrlFormat);
        void bindStart(TextField tfStart);
        void bindEnd(TextField tfEnd);
        void bindSaveLocation(TextField tfSaveLocation);
        void bindCookieKey(TextField tfCookieKey);
        void bindCookieValue(TextField tfCookieValue);
        void bindCookieTableView(TableView<CookieViewModel> tableCookie);
        Optional<File> getRecentSaveDir();
        void setSaveLocation(File saveFile);
        void changeBitrate(int bitrate);
        void addCookie();
        void removeCookie();
        void addDownloadTask();
        void autoComplete();
        void setUrlFormat(String urlFormat);
        void setStart(int start);
        void setEnd(int end);
    }
}