package me.moonchan.streaming.downloader.ui;

public class BaseContract {

    public interface Presenter<T> {
        void setView(T view);
    }

    public interface View {

    }
}
