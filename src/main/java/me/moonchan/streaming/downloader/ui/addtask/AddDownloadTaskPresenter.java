package me.moonchan.streaming.downloader.ui.addtask;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.streaming.downloader.domain.Cookie;
import me.moonchan.streaming.downloader.domain.DownloadInfo;
import me.moonchan.streaming.downloader.domain.DownloadUrl;
import me.moonchan.streaming.downloader.util.Constants;
import me.moonchan.streaming.downloader.util.JsonPreferences;
import me.moonchan.streaming.downloader.event.AddDownloadInfoEvent;
import me.moonchan.streaming.downloader.util.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AddDownloadTaskPresenter implements AddDownloadTaskContract.Presenter {
    private final StringProperty url;
    private final StringProperty urlFormat;
    private final StringProperty start;
    private final StringProperty end;
    private final StringProperty saveLocation;
    private final StringProperty cookieKey;
    private final StringProperty cookieValue;

    private File recentSaveDir;
    private ObservableList<CookieViewModel> cookieData;
    private EventBus eventBus;
    private JsonPreferences preferences;

    @Autowired
    public AddDownloadTaskPresenter(EventBus eventBus, JsonPreferences preferences) {
        this.url = new SimpleStringProperty("");
        this.urlFormat = new SimpleStringProperty("");
        this.start = new SimpleStringProperty("");
        this.end = new SimpleStringProperty("");
        this.saveLocation = new SimpleStringProperty("");
        this.cookieKey = new SimpleStringProperty("");
        this.cookieValue = new SimpleStringProperty("");
        this.cookieData = FXCollections.observableArrayList();
        this.eventBus = eventBus;
        this.preferences = preferences;
    }

    @Override
    public void init() {
        clearData();
        String clipboardText = Clipboard.getSystemClipboard().getString();
        if (clipboardText != null) {
            autoFill(clipboardText);
            if (!urlFormat.get().isEmpty())
                url.set(clipboardText);
        }
        // 최근 쿠키 설정
        Optional<Cookie> cookie = preferences.getObject(Constants.PreferenceKey.PREF_RECENT_COOKIE, Cookie.class);
        cookie.ifPresent(this::setCookie);
        // 최근 저장 위치 설정
        String recentSaveDirPath = preferences.get(Constants.PreferenceKey.PREF_RECENT_SAVE_FILE, "");
        if (!recentSaveDirPath.isEmpty()) {
            setRecentSaveFile(recentSaveDirPath);
        }
    }

    private void clearData() {
        cookieData.clear();
        url.set("");
        urlFormat.set("");
        start.set("");
        end.set("");
    }

    private void setRecentSaveFile(String path) {
        File recentSaveFile = new File(path);
        saveLocation.set(recentSaveFile.getAbsolutePath());
        this.recentSaveDir = recentSaveFile.getParentFile();
    }

    private boolean hasRecentSaveDir() {
        return recentSaveDir != null && recentSaveDir.isDirectory();
    }

    public Optional<File> getRecentSaveDir() {
        if (hasRecentSaveDir())
            return Optional.of(recentSaveDir);
        return Optional.empty();
    }

    @Override
    public void setSaveLocation(File save) {
        this.saveLocation.set(save.getAbsolutePath());
        recentSaveDir = save.getParentFile();
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

    private void setCookie(Cookie cookie) {
        List<CookieViewModel> cookieViewModelList = cookie.getCookieViewModelList();
        cookieData.addAll(cookieViewModelList);
    }

    private Cookie getCookie() {
        if (cookieData.isEmpty())
            return null;
        Cookie cookie = new Cookie();
        cookieData.forEach(v -> cookie.putCookie(v.getKey().getValue(), v.getValue().getValue()));
        return cookie;
    }

    private void autoFill(String url) {
        log.debug(url);
        Optional<DownloadUrl> downloadUrl = toDownloadUrl(url);
        downloadUrl.ifPresent(v -> {
            urlFormat.set(v.getUrlFormat());
            start.set(String.valueOf(v.getStart()));
            end.set(String.valueOf(v.getEnd()));
        });
    }

    private Optional<DownloadUrl> toDownloadUrl(String url) {
        if (!(url.startsWith("http://") || url.startsWith("https://")))
            return Optional.empty();
        try {
            int start = preferences.getInt(Constants.PreferenceKey.PREF_RECENT_START, 1);
            return Optional.of(DownloadUrl.of(url, start));
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
        if (!saveDir.isDirectory()) {
            return false;
        }
        return saveLocation.getName().endsWith(".ts");
    }

    public void addDownloadTask() {
        if (!validateSaveLocation()) {
            throw new RuntimeException("저장 위치가 올바르지 않습니다.");
        }
        File saveLocation = new File(this.saveLocation.get());
        DownloadUrl downloadUrl = getDownloadUrl();
        Cookie cookie = getCookie();
        int start = Integer.parseInt(this.start.get());
        eventBus.send(AddDownloadInfoEvent.of(new DownloadInfo(downloadUrl, saveLocation, cookie, start)));
    }

    @Override
    public void autoComplete() {
        autoFill(url.get());
    }

    @Override
    public void setView(AddDownloadTaskContract.View view) {

    }

    @Override
    public void bindUrl(TextField tfUrl) {
        tfUrl.textProperty().bindBidirectional(url);
    }

    @Override
    public void bindUrlFormat(TextField tfUrlFormat) {
        tfUrlFormat.textProperty().bindBidirectional(urlFormat);
    }

    @Override
    public void bindStart(TextField tfStart) {
        tfStart.textProperty().bindBidirectional(start);
    }

    @Override
    public void bindEnd(TextField tfEnd) {
        tfEnd.textProperty().bindBidirectional(end);
    }

    @Override
    public void bindSaveLocation(TextField tfSaveLocation) {
        tfSaveLocation.textProperty().bindBidirectional(saveLocation);
    }

    @Override
    public void bindCookieKey(TextField tfCookieKey) {
        tfCookieKey.textProperty().bindBidirectional(cookieKey);
    }

    @Override
    public void bindCookieValue(TextField tfCookieValue) {
        tfCookieValue.textProperty().bindBidirectional(cookieValue);
    }

    @Override
    public void bindCookieTableView(TableView<CookieViewModel> tableCookie) {
        tableCookie.setItems(cookieData);
    }
}
