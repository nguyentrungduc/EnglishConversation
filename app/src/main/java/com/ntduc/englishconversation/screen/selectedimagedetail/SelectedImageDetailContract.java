package com.ntduc.englishconversation.screen.selectedimagedetail;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface SelectedImageDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
    }
}
