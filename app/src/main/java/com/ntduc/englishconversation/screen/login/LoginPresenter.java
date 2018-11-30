package com.ntduc.englishconversation.screen.login;

import android.text.TextUtils;
import com.facebook.AccessToken;
import com.ntduc.englishconversation.R;
import com.ntduc.englishconversation.data.source.callback.DataCallback;
import com.ntduc.englishconversation.data.source.local.sharedprf.SharedPrefsApi;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.ntduc.englishconversation.utils.Constant;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

import static com.ntduc.englishconversation.data.source.local.sharedprf.SharedPrefsKey.PREF_EMAIL;
import static com.ntduc.englishconversation.data.source.local.sharedprf.SharedPrefsKey
        .PREF_PASSWORD;

/**
 * Listens to user actions from the UI ({@link LoginActivity}), retrieves the data and updates
 * the UI as required.
 */
final class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;
    private DataCallback<FirebaseUser> mSignInCallback;
    private SharedPrefsApi mSharedPrefs;

    public LoginPresenter(LoginContract.ViewModel viewModel, AuthenicationRepository repository,
            SharedPrefsApi sharedPrefs) {
        mViewModel = viewModel;
        mRepository = repository;
        mSharedPrefs = sharedPrefs;
        initSignInCallback();
    }

    @Override
    public void onStart() {
        mRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccessful(data);
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetCurrentUserError(msg);
                mViewModel.onGetLastEmail(mSharedPrefs.get(PREF_EMAIL, String.class));
                mViewModel.onGetLastPassword(mSharedPrefs.get(PREF_PASSWORD, String.class));
            }
        });
    }

    private void initSignInCallback() {
        mSignInCallback = new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccessful(data);
                mViewModel.dismissDialog();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.dismissDialog();
                mViewModel.onLoginError(msg);
            }
        };
    }

    @Override
    public void onStop() {
    }

    @Override
    public void login(final String email, final String password, final boolean isRememberAccount) {
        mViewModel.showDialog();
        mRepository.signIn(email, password, new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccessful(data);
                mViewModel.dismissDialog();
                if (isRememberAccount) {
                    mSharedPrefs.put(PREF_EMAIL, email);
                    mSharedPrefs.put(PREF_PASSWORD, password);
                } else {
                    mSharedPrefs.put(PREF_EMAIL, "");
                    mSharedPrefs.put(PREF_PASSWORD, "");
                }
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.dismissDialog();
                mViewModel.onLoginError(msg);
            }
        });
    }

    @Override
    public void login(GoogleSignInAccount account) {
        mViewModel.showDialog();
        mRepository.signIn(account, mSignInCallback);
    }

    @Override
    public void login(AccessToken accessToken) {
        mViewModel.showDialog();
        mRepository.signIn(accessToken, mSignInCallback);
    }

    @Override
    public boolean validateInput(String email, String password) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            isValid = false;
            mViewModel.onInputEmailError(R.string.is_empty);
        }
        if (TextUtils.isEmpty(password)) {
            isValid = false;
            mViewModel.onInputPasswordError(R.string.is_empty);
        }
        if (!TextUtils.isEmpty(email) && !email.matches(Constant.EMAIL_FORMAT)) {
            isValid = false;
            mViewModel.onInputEmailError(R.string.invalid_email_format);
        }
        if (!TextUtils.isEmpty(password)
                && password.length() < Constant.MINIMUM_CHARACTERS_PASSWORD) {
            isValid = false;
            mViewModel.onInputPasswordError(R.string.least_6_characters);
        }
        return isValid;
    }
}
