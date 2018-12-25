package com.ntduc.englishconversation.screen.audiodetail;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;



public interface AudioDetailContract {

    /**
     * Presenter of Detail
     */
    interface Presenter extends BasePresenter {

    }

    /**
     * View of Detail
     */
    interface View extends BaseViewModel<Presenter> {

        void onFinishActivity();

        void onResume();

        void onPause();
    }

}
