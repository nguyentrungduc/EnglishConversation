package com.ntduc.englishconversation.data.source;

import com.ntduc.englishconversation.data.model.Setting;



public interface SettingDataSource {
    Setting getSetting();

    void saveSetting(Setting setting);
}
