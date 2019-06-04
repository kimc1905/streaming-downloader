package me.moonchan.streaming.downloader.ui.addtask;

import com.jakewharton.rxrelay2.PublishRelay;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import me.moonchan.streaming.downloader.Cookie;
import me.moonchan.streaming.downloader.DownloadInfo;
import me.moonchan.streaming.downloader.DownloadUrl;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Getter
@Log4j
public class AddDownloadTaskViewModel {

    private final StringProperty url = new SimpleStringProperty("");
    private final StringProperty urlFormat = new SimpleStringProperty("");
    private final StringProperty start = new SimpleStringProperty("");
    private final StringProperty end = new SimpleStringProperty("");
    private final StringProperty saveLocation = new SimpleStringProperty("");
    private final StringProperty cookieKey = new SimpleStringProperty("");
    private final StringProperty cookieValue = new SimpleStringProperty("");

    private File recentSaveDir;
    private ObservableList<CookieViewModel> cookieData;
    PublishRelay<DownloadInfo> relayDownloadInfo;

    public AddDownloadTaskViewModel() {
        relayDownloadInfo = PublishRelay.create();
        cookieData = FXCollections.observableArrayList();
    }

    public void setRecentSaveFile(String path) {
        File recentSaveFile = new File(path);
        saveLocation.set(recentSaveFile.getAbsolutePath());
        this.recentSaveDir = recentSaveFile.getParentFile();
    }

    public boolean hasRecentSaveDir() {
        return recentSaveDir != null && recentSaveDir.isDirectory();
    }

    private int searchCookie(String key) {
        for (int i = 0; i < cookieData.size(); i++) {
            CookieViewModel cookieViewModel = cookieData.get(i);
            if (cookieViewModel.isSameKey(key))
                return i;
        }
        return -1;
    }

    public void addCookie() {
        String key = cookieKey.get();
        String value = cookieValue.get();
        if (key.isEmpty() || value.isEmpty())
            return;

        int index = searchCookie(key);
        CookieViewModel cookieViewModel = new CookieViewModel(key, value);
        if (index < 0)
            cookieData.add(cookieViewModel);
        else
            cookieData.set(index, cookieViewModel);
    }

    public void removeCookie() {
        String key = cookieKey.get();

        if (key.isEmpty())
            return;

        int index = searchCookie(key);
        if (index >= 0)
            cookieData.remove(index);
    }

    public void setCookie(Cookie cookie) {
        List<CookieViewModel> cookieViewModelList = cookie.getCookieViewModelList();
        cookieData.addAll(cookieViewModelList);
    }

    public Cookie getCookie() {
        if(cookieData.isEmpty())
            return null;
        Cookie cookie = new Cookie();
        cookieData.forEach(v -> cookie.putCookie(v.getKey().getValue(), v.getValue().getValue()));
        return cookie;
    }

    public void browseSaveLocation(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TS File", "*.ts")
        );

        if(hasRecentSaveDir()) {
            fileChooser.setInitialDirectory(getRecentSaveDir());
        }

        File saveFile = fileChooser.showSaveDialog(window);
        if (saveFile != null) {
            saveLocation.set(saveFile.getAbsolutePath());
            recentSaveDir = saveFile.getParentFile();
        }
    }

    public void setDownloadUrl(String url) {
        log.debug(url);
        Optional<DownloadUrl> downloadUrl = toDownloadUrl(url);
        downloadUrl.map(DownloadUrl::getUrlFormat).ifPresent(urlFormat::set);
        downloadUrl.map(DownloadUrl::getStart).map(String::valueOf).ifPresent(start::set);
        downloadUrl.map(DownloadUrl::getEnd).map(String::valueOf).ifPresent(end::set);
    }

    private Optional<DownloadUrl> toDownloadUrl(String url) {
        if (!(url.startsWith("http://") || url.startsWith("https://")))
            return Optional.empty();
        try {
            return Optional.of(DownloadUrl.of(url));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private DownloadUrl getDownloadUrl() {
        String urlFormat = this.urlFormat.get();
        int start = Integer.parseInt(this.start.get());
        int end = Integer.parseInt(this.end.get());

        return DownloadUrl.of(urlFormat, start, end);
    }

    private boolean validateSaveLocation() {
        String saveLocationText = saveLocation.get();
        if (saveLocationText.isEmpty())
            throw new RuntimeException("save location is empty");
        File saveLocation = new File(saveLocationText);
        File saveDir = saveLocation.getParentFile();
        if(!saveDir.isDirectory()) {
            return false;
        }
        if(!saveLocation.getName().endsWith(".ts")) {
            return false;
        }
        return true;
    }

    public void addDownloadTask() throws Exception {
        if(!validateSaveLocation()) {
            throw new RuntimeException("저장 위치가 올바르지 않습니다.");
        }
        File saveLocation = new File(this.saveLocation.get());
        DownloadUrl downloadUrl = getDownloadUrl();
        Cookie cookie = getCookie();
        relayDownloadInfo.accept(new DownloadInfo(downloadUrl, saveLocation, cookie));
    }
}
