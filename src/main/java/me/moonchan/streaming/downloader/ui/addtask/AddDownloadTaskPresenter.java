package me.moonchan.streaming.downloader.ui.addtask;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.streaming.downloader.domain.*;
import me.moonchan.streaming.downloader.event.AddDownloadInfoEvent;
import me.moonchan.streaming.downloader.exception.UrlParseException;
import me.moonchan.streaming.downloader.util.AppPreferences;
import me.moonchan.streaming.downloader.util.EventBus;
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
    private final StringProperty urlFormat;
    private final StringProperty start;
    private final StringProperty end;
    private final StringProperty saveLocation;
    private final StringProperty cookieKey;
    private final StringProperty cookieValue;

    private File recentSaveDir;
    private DownloadUrl downloadUrl;
    private ObservableList<CookieViewModel> cookieData;
    private EventBus eventBus;
    private AppPreferences preferences;
    private AddDownloadTaskContract.View view;

    @Autowired
    public AddDownloadTaskPresenter(EventBus eventBus, AppPreferences preferences) {
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
        Optional<Cookie> cookie = preferences.getRecentCookie();
        cookie.ifPresent(this::setCookie);
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
        urlFormat.set("");
        start.set("");
        end.set("");
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
        downloadUrl.getBitrate().ifPresent(v -> urlFormat.set(this.downloadUrl.getUrlFormat()));
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
        try {
            downloadUrl = DownloadUrlFactory.create(url);
            urlFormat.set(downloadUrl.getUrlFormat());
            start.set(String.valueOf(downloadUrl.getStart()));
//            start.set(String.valueOf(preferences.getRecentStart()));
            end.set(String.valueOf(downloadUrl.getEnd()));
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
//            e.printStackTrace();
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
        return saveLocation.getName().endsWith(".ts");
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
    public void setUrlFormat(String urlFormat) {
        downloadUrl.setUrlFormat(urlFormat);
    }

    @Override
    public void setStart(int start) {
        downloadUrl.setStart(start);
    }

    @Override
    public void setEnd(int end) {
        downloadUrl.setEnd(end);
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