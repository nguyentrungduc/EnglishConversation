package com.ntduc.englishconversation.screen.setting;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ntduc.englishconversation.BR;
import com.ntduc.englishconversation.R;
import com.ntduc.englishconversation.data.model.Setting;
import com.ntduc.englishconversation.data.model.UserModel;
import com.ntduc.englishconversation.screen.changepassword.ChangePasswordFragment;
import com.ntduc.englishconversation.screen.login.LoginActivity;
import com.ntduc.englishconversation.screen.main.MainActivity;
import com.ntduc.englishconversation.utils.navigator.Navigator;
import com.google.android.gms.common.api.GoogleApiClient;


public class SettingViewModel extends BaseObservable implements SettingContract.ViewModel {
    private SettingContract.Presenter mPresenter;
    private UserModel mCurrentUser;
    private Navigator mNavigator;
    private MainActivity mActivity;
    private Setting mSetting;
    private boolean mAllowChangePassword;

    public SettingViewModel(MainActivity activity) {
        mNavigator = new Navigator(activity);
        mActivity = activity;
    }

    @Bindable
    public boolean isAllowChangePassword() {
        return mAllowChangePassword;
    }

    public void setAllowChangePassword(boolean allowChangePassword) {
        mAllowChangePassword = allowChangePassword;
        notifyPropertyChanged(BR.allowChangePassword);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onLogoutClick() {
        mPresenter.signOut();
    }

    public void onChangePasswordClick() {
        ChangePasswordFragment.newInstance()
                .show((mActivity).getSupportFragmentManager(), null);
    }

    @Bindable
    public UserModel getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(UserModel currentUser) {
        mCurrentUser = currentUser;
        notifyPropertyChanged(BR.currentUser);
    }

    @Override
    public void onSignOutSuccess() {
        mNavigator.startActivity(
                LoginActivity.getInstance(mActivity).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    public void onSignOutFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onGetCurrentUserSuccess(UserModel userModel) {
        setCurrentUser(userModel);
    }

    @Override
    public void onGetUserFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public GoogleApiClient getGoogleApiCliennt() {
        return mActivity.getGoogleApiClient();
    }

    @Override
    public void onGetSettingSuccess(Setting setting) {
        setSetting(setting);
    }

    @Bindable
    public Setting getSetting() {
        return mSetting;
    }

    public void setSetting(Setting setting) {
        mSetting = setting;
        notifyPropertyChanged(BR.setting);
    }

    public void onCheckBoxChanged() {
        mSetting.setEnableAutoPlay(!mSetting.isEnableAutoPlay());
        mPresenter.saveSetting(mSetting);
        mNavigator.showToast(R.string.msg_change_setting_enable_success);
    }

}
