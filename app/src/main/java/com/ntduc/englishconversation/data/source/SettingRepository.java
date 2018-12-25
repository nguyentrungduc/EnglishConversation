package com.ntduc.englishconversation.data.source;

import com.ntduc.englishconversation.data.model.Setting;

public class SettingRepository implements SettingDataSource {
    private SettingDataSource mLocalDataSource;

    public SettingRepository(SettingDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public Setting getSetting() {
        return mLocalDataSource.getSetting();
    }

    @Override
    public void saveSetting(Setting setting) {
        mLocalDataSource.saveSetting(setting);
    }
}
