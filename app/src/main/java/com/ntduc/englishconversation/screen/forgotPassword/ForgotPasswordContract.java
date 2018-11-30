package com.ntduc.englishconversation.screen.forgotPassword;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ForgotPasswordContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onResetPasswordClick();

        void onInputEmailError(int message);

        void onResetPasswordSuccess();

        void onResetPasswordFailed(String msg);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void resetPassword(String email);

        boolean validateInput(String email);
    }
}
