package com.ntduc.englishconversation.data.source.local.realm;

/**
 *
 */

public interface BaseLocalDataSource {
    void openTransaction();

    void closeTransaction();
}
