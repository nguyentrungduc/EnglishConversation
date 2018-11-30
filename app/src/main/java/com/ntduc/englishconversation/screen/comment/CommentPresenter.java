package com.ntduc.englishconversation.screen.comment;

import com.ntduc.englishconversation.data.model.Comment;
import com.ntduc.englishconversation.data.source.callback.DataCallback;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.ntduc.englishconversation.data.source.remote.comment.CommentRepository;
import com.ntduc.englishconversation.utils.Utils;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.ntduc.englishconversation.data.model.Status.ADD;
import static com.ntduc.englishconversation.data.model.Status.DELETE;
import static com.ntduc.englishconversation.data.model.Status.EDIT;

/**
 * Listens to user actions from the UI ({@link CommentFragment}), retrieves the data and updates
 * the UI as required.
 */
final class CommentPresenter implements CommentContract.Presenter {
    private static final String TAG = CommentPresenter.class.getName();
    private CommentRepository mRepository;
    private final CommentContract.ViewModel mViewModel;
    private CompositeDisposable mDisposable;
    private Comment mLastComment;

    CommentPresenter(CommentContract.ViewModel viewModel, CommentRepository commentRepository) {
        mViewModel = viewModel;
        mRepository = commentRepository;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        mRepository.removeListener();
        mDisposable.dispose();
    }

    @Override
    public void fetchCommentData(final Comment lastComment) {
        mDisposable.add(mRepository.getComment(lastComment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Comment>>() {
                    @Override
                    public void onNext(List<Comment> comments) {
                        mViewModel.onGetCommentsSuccess(comments);
                        mLastComment = comments.isEmpty() ? mLastComment
                                : comments.get(comments.size() - 1);
                        // remove listener old
                        mRepository.removeListener();
                        registerModifyComment(mLastComment);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetCommentsFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void saveCommentDelete(Comment comment) {
        mRepository.saveCommentDelete(comment)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void deleteComment(Comment comment) {
        //// TODO: 1/2/2018  handle save history update comment
        mViewModel.showDialogComment();
        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
        comment.setModifiedAt(Utils.generateOppositeNumber(
                System.currentTimeMillis()));
        mDisposable.add(mRepository.deleteComment(comment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Comment>() {
                    @Override
                    public void onNext(Comment comment) {
                        mViewModel.deleComentSuccess(comment);
                        saveCommentDelete(comment);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetCommentsFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void initDialogItem(final Comment comment) {
        final String idCreateUser = comment.getCreateUser().getId();
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        repository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser firebaseUser) {
                if (idCreateUser.equals(firebaseUser.getUid())) {
                    mViewModel.showPopupMenuComment(comment);
                }
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });
    }

    private void registerModifyComment(Comment lastComment) {
        mDisposable.add(mRepository.registerModifyTimelines(lastComment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<HashMap<Integer, Comment>>() {
                    @Override
                    public void onNext(HashMap<Integer, Comment> commentHashMap) {
                        if (commentHashMap.containsKey(EDIT)) {
                            mViewModel.onUpdateComment(commentHashMap.get(EDIT));
                        }
                        if (commentHashMap.containsKey(ADD)) {
                            mViewModel.onAddComment(commentHashMap.get(ADD));
                        }
                        if (commentHashMap.containsKey(DELETE)) {
                            mViewModel.onDeleteComment(commentHashMap.get(DELETE));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetCommentsFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
