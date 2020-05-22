package me.moonchan.ts.downloader.gui.ui.addtask;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.core.domain.model.Bitrate;
import me.moonchan.ts.downloader.core.domain.model.Cookie;
import me.moonchan.ts.downloader.core.DownloadInfo;
import me.moonchan.ts.downloader.core.domain.url.DownloadUrl;
import me.moonchan.ts.downloader.core.domain.url.DownloadUrlFactory;
import me.moonchan.ts.downloader.core.domain.url.WavveDownloadUrl;
import me.moonchan.ts.downloader.gui.event.AddDownloadInfoEvent;
import me.moonchan.ts.downloader.core.exception.UrlParseException;
import me.moonchan.ts.downloader.gui.util.AppPreferences;
import me.moonchan.ts.downloader.gui.util.CookieUtil;
import me.moonchan.ts.downloader.gui.util.EventBus;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Component
@Slf4j
public class AddDownloadTaskPresenter implements AddDownloadTaskContract.Presenter {
    private final StringProperty url;
    private final StringProperty m3u8Url;
    private final StringProperty saveLocation;
    private final StringProperty cookieKey;
    private final StringProperty cookieValue;

    private File recentSaveDir;
    private DownloadUrl downloadUrl;
    private ObservableList<CookieViewModel> cookieData;
    private EventBus eventBus;
    private AppPreferences preferences;
    private OkHttpClient client;
    private AddDownloadTaskContract.View view;

    @Autowired
    public AddDownloadTaskPresenter(EventBus eventBus, AppPreferences preferences) {
        this.url = new SimpleStringProperty("");
        this.m3u8Url = new SimpleStringProperty("");
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
            if (!m3u8Url.get().isEmpty())
                url.set(clipboardText);
        }
        // 최근 저장 위치 설정
        String recentSaveDirPath = preferences.getRecentSaveFile();
        if (!recentSaveDirPath.isEmpty()) {
            setRecentSaveFile(recentSaveDirPath);
        }
    }

    private void clearData() {
        this.downloadUrl = new DownloadUrl();
        cookieData.clear();
        url.set("");
        m3u8Url.set("");
        cookieKey.set("");
        cookieValue.set("");
        view.showBitrateBox(false);
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

    @Override
    public void changeBitrate(Bitrate bitrate) {
        if(!downloadUrl.hasBitrate()) {
            return;
        }
        downloadUrl.setBitrate(bitrate);
        downloadUrl.getBitrate().ifPresent(v -> m3u8Url.set(this.downloadUrl.getM3u8Url()));
    }

    private OptionalInt searchCookie(String key) {
        for (int i = 0; i < cookieData.size(); i++) {
            CookieViewModel cookieViewModel = cookieData.get(i);
            if (cookieViewModel.isSameKey(key))
                return OptionalInt.of(i);
        }
        return OptionalInt.empty();
    }

    public void addCookie() {
        String key = cookieKey.get();
        String value = cookieValue.get();
        if (key.isEmpty() || value.isEmpty())
            return;
        CookieViewModel cookieViewModel = new CookieViewModel(key, value);
        OptionalInt cookieIndex = searchCookie(key);
        if (cookieIndex.isPresent())
            cookieData.set(cookieIndex.getAsInt(), cookieViewModel);
        else
            cookieData.add(cookieViewModel);
    }

    public void removeCookie() {
        String key = cookieKey.get();
        if (key.isEmpty())
            return;
        searchCookie(key).ifPresent(index -> cookieData.remove(index));
    }

    private void setCookie(Cookie cookie) {
        List<CookieViewModel> cookieViewModelList = CookieUtil.getCookieViewModelList(cookie);
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
        try {
            downloadUrl = DownloadUrlFactory.create(url);
            m3u8Url.set(downloadUrl.getM3u8Url());
            cookieData.clear();
            if(downloadUrl instanceof WavveDownloadUrl) {
                cookieData.add(new CookieViewModel("authtoken", ""));
                cookieKey.setValue("authtoken");
                cookieValue.setValue("");
            }
            if(downloadUrl.hasBitrate()) {
                view.showBitrateBox(true);
                downloadUrl.getBitrate().ifPresent(v -> {
                    Bitrate bitrate = preferences.getRecentBitrate(v);
                    view.setSelectBitrate(bitrate);
                });
            } else {
                view.showBitrateBox(false);
            }
        } catch (UrlParseException e) {
            log.error(e.getMessage());
        }
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
        String savePath = saveLocation.getName();
        return savePath.endsWith(".ts") || savePath.endsWith(".m4s");
    }

    public void addDownloadTask() {
        if (!validateSaveLocation()) {
            throw new RuntimeException("저장 위치가 올바르지 않습니다.");
        }
        File saveLocation = new File(this.saveLocation.get());
        Cookie cookie = getCookie();
        eventBus.send(AddDownloadInfoEvent.of(new DownloadInfo(downloadUrl, saveLocation, cookie)));
    }

    @Override
    public void autoComplete() {
        autoFill(url.get());
    }

    @Override
    public void setM3u8Url(String m3u8Url) {
        downloadUrl.setM3u8Url(m3u8Url);
    }

    @Override
    public void setView(AddDownloadTaskContract.View view) {
        this.view = view;
    }

    @Override
    public void bindUrl(TextField tfUrl) {
        tfUrl.textProperty().bindBidirectional(url);
    }

    @Override
    public void bindUrlFormat(TextField tfUrlFormat) {
        tfUrlFormat.textProperty().bindBidirectional(m3u8Url);
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