package com.ntduc.englishconversation.screen.conversationdetail;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;


public interface ConversationDetailContract {

    /**
     * base presenter for ConversationDetailPresenter
     */
    interface Presenter extends BasePresenter {

    }

    /**
     * base presenter for ConversationDetailViewModel
     */
    interface ViewModel extends BaseViewModel<Presenter> {

        void onResume();

        void onPause();
    }
}
