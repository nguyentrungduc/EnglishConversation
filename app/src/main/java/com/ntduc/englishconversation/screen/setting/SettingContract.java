package com.ntduc.englishconversation.screen.setting;

/**
 * Created by doan.van.toan on 1/16/18.
 */

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;
import com.ntduc.englishconversation.data.model.Setting;
import com.ntduc.englishconversation.data.model.UserModel;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Define contract of SettingViewModel and SettingPresneter
 */
public interface SettingContract {
    /**
     * Setting ViewModel
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onSignOutSuccess();

        void onSignOutFailed(String msg);

        void onGetCurrentUserSuccess(UserModel userModel);

        void onGetUserFailed(String msg);

        GoogleApiClient getGoogleApiCliennt();

        void onGetSettingSuccess(Setting setting);

        void setAllowChangePassword(boolean b);
    }

    /**
     * Setting Presenter
     */
    interface Presenter extends BasePresenter {
        void signOut();

        void getSetting();

        void saveSetting(Setting setting);
    }
}
