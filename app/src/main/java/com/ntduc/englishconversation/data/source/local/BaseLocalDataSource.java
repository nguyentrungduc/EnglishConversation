package com.ntduc.englishconversation.data.source.local;

import com.ntduc.englishconversation.data.source.local.realm.RealmApi;

/**
 *
 */

public class BaseLocalDataSource {
    private RealmApi mApi;

    public BaseLocalDataSource(RealmApi api) {
        mApi = api;
    }
}
