package com.ntduc.englishconversation.screen.login;

import android.content.Intent;
import com.facebook.AccessToken;
import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

/**
 * This specifies the contract between the view and the presenter.
 */
interface LoginContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onRegisterClick();

        void showDialog();

        void dismissDialog();

        void onGetCurrentUserError(String message);

        void onGetUserSuccessful(FirebaseUser firebaseUser);

        void onLoginError(String message);

        void onLoginClick();

        void onLoginGoogleClick();

        void onForgotPasswordClick();

        void onInputEmailError(int message);

        void onInputPasswordError(int message);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onLoginFacebookSuccess(AccessToken accessToken);

        void onGetLastEmail(String s);

        void onGetLastPassword(String s);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void login(String email, String password, boolean isRememberAccount);

        void login(GoogleSignInAccount account);

        void login(AccessToken accessToken);

        boolean validateInput(String email, String password);
    }
}
