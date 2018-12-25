package com.ntduc.englishconversation.data.source.callback;

public interface DataCallback<T> {
    void onGetDataSuccess(T data);

    void onGetDataFailed(String msg);
}
