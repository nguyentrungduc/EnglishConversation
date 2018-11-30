package com.ntduc.englishconversation.screen.audiodetail;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;

/**
 * Created by fs-sournary.
 * Date on 12/19/17.
 * Description:
 */

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
