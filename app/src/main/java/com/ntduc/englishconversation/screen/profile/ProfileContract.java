package com.ntduc.englishconversation.screen.profile;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;
import com.ntduc.englishconversation.data.model.UserModel;

;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ProfileContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetUserSuccesss(UserModel data);

        void onChangeAvtClick();

        void onEditUserClick();

        void showChangePasswordDialog();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
    }
}
