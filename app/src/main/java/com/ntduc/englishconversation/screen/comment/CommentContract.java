package com.ntduc.englishconversation.screen.comment;

import com.ntduc.englishconversation.BaseFragment;
import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;
import com.ntduc.englishconversation.data.model.Comment;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface CommentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void showDialogComment();

        void onPause();

        void onResume();

        void onDestroy();

        void onGetCommentsSuccess(List<Comment> comments);

        void onGetCommentsFailure(String message);

        void onGetCommentSuccess(Comment comment);

        void onAddComment(Comment comment);

        void onDeleteComment(Comment comment);

        void onUpdateComment(Comment comment);

        void showPopupMenuComment(Comment comment);

        void deleComentSuccess(Comment comment);

        void replaceFragment(BaseFragment baseFragment);

        void onCommmentSuccess();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onPause();

        void onResume();

        void onDestroy();

        void fetchCommentData(Comment lastComments);

        void deleteComment(Comment comment);

        void initDialogItem(Comment comment);
    }
}
