package me.moonchan.ts.downloader.gui.ui;

public class BaseContract {

    public interface Presenter<T> {
        void setView(T view);
    }

    public interface View {

    }
}