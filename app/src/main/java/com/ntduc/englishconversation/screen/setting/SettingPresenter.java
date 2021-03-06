package com.ntduc.englishconversation.screen.setting;

import com.facebook.login.LoginManager;
import com.ntduc.englishconversation.data.model.Setting;
import com.ntduc.englishconversation.data.model.UserModel;
import com.ntduc.englishconversation.data.source.SettingRepository;
import com.ntduc.englishconversation.data.source.callback.DataCallback;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.ntduc.englishconversation.utils.Constant;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

public class SettingPresenter implements SettingContract.Presenter {

    private AuthenicationRepository mAuthenicationRepository;
    private SettingRepository mSettingRepository;
    private SettingContract.ViewModel mViewModel;
    private DataCallback mSignOutCallback;
    private FirebaseUser mUser;

    public SettingPresenter(SettingContract.ViewModel viewModel,
                            AuthenicationRepository authenicationRepository,
                            SettingRepository settingRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
        mSettingRepository = settingRepository;
        getUser();
        getSetting();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    private void getUser() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mUser = data;
                mViewModel.onGetCurrentUserSuccess(new UserModel(data));
                mViewModel.setAllowChangePassword(checkAccountSignUpWithEmailPassword(data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetUserFailed(msg);
            }
        });
    }

    private boolean checkAccountSignUpWithEmailPassword(FirebaseUser firebaseUser) {
        return firebaseUser.getProviders().contains(Constant.PASSWORD);

    }

    @Override
    public void getSetting() {
        mViewModel.onGetSettingSuccess(mSettingRepository.getSetting());
    }

    @Override
    public void saveSetting(Setting setting) {
        mSettingRepository.saveSetting(setting);
    }

    private void initSignOutCallback() {
        mSignOutCallback = new DataCallback() {
            @Override
            public void onGetDataSuccess(Object data) {
                mViewModel.onSignOutSuccess();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onSignOutFailed(msg);
            }
        };
    }

    @Override
    public void signOut() {
        if (mUser == null) {
            return;
        }
        if (mSignOutCallback == null) {
            initSignOutCallback();
        }
        List<String> provinders = mUser.getProviders();
        if (provinders != null && provinders.size() != 0) {
            for (String provinder : provinders) {
                switch (provinder) {
                    case GoogleAuthProvider.PROVIDER_ID:
                        mAuthenicationRepository.signOut(
                                mViewModel.getGoogleApiCliennt(),
                                mSignOutCallback);
                        break;
                    case FacebookAuthProvider.PROVIDER_ID:
                        mAuthenicationRepository.signOut(
                                LoginManager.getInstance(),
                                mSignOutCallback);
                        break;
                    default:
                        mAuthenicationRepository.signOut(mSignOutCallback);
                        break;
                }
            }
        }
    }
}
