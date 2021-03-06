package com.ntduc.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ntduc.englishconversation.BR;


public class Setting extends BaseObservable {
    private boolean mIsEnableAutoPlay;

    @Bindable
    public boolean isEnableAutoPlay() {
        return mIsEnableAutoPlay;
    }

    public void setEnableAutoPlay(boolean enableAutoPlay) {
        mIsEnableAutoPlay = enableAutoPlay;
        notifyPropertyChanged(BR.enableAutoPlay);
    }
}
