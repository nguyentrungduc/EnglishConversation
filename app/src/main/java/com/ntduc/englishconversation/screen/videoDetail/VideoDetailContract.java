package com.ntduc.englishconversation.screen.videoDetail;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface VideoDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onPause();

        void onResume();

        void finishActivity();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onPause();

        void onResume();
    }
}
