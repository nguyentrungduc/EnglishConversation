package com.ntduc.englishconversation.screen.comment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.ntduc.englishconversation.BR;
import com.ntduc.englishconversation.BaseFragment;
import com.ntduc.englishconversation.R;
import com.ntduc.englishconversation.data.model.Comment;
import com.ntduc.englishconversation.data.model.UserModel;
import com.ntduc.englishconversation.screen.createcomment.CreateCommentFragment;
import com.ntduc.englishconversation.screen.editcomment.EditCommentFragment;
import com.ntduc.englishconversation.screen.profileuser.ProfileUserActivity;
import com.ntduc.englishconversation.screen.selectedimagedetail.SelectedImageDetailActivity;
import com.ntduc.englishconversation.screen.timeline.OnTimelineItemTouchListener;
import com.ntduc.englishconversation.utils.OnEndScrollListener;
import com.ntduc.englishconversation.utils.navigator.Navigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exposes the data to be used in the Comment screen.
 */

public class CommentViewModel extends BaseObservable
        implements CommentContract.ViewModel, OnEndScrollListener.OnEndScroll,
        OnTimelineItemTouchListener<Comment> {
    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String DIMEN = "dimen";
    private static final String ANDROID = "android";
    private CommentContract.Presenter mPresenter;
    private CommentAdapter mAdapter;
    private Context mContext;
    private OnEndScrollListener mOnEndScrollListener;
    private Navigator mNavigator;
    private String mTimelineModelId;
    private int mDefaultHeight;
    private FragmentManager mManager;
    private Fragment mFragment;
    private UserModel mTimelineUser;
    private boolean mIsLoadingMore;
    private View mViewGroupComment;
    private ProgressDialog mProgressDialog;
    private int mScrollPosition;


    public CommentViewModel(Context context, String timelineModelId, FragmentManager manager,
                            UserModel userModel) {
        mAdapter = new CommentAdapter(new ArrayList<Comment>(), this);
        mContext = context;
        mOnEndScrollListener = new OnEndScrollListener(this);
        mNavigator = new Navigator((Activity) mContext);
        mTimelineModelId = timelineModelId;
        mDefaultHeight =
                ((Activity) context).getWindow().getDecorView().getHeight() - getStatusBarHeight(
                        context);
        mManager = manager;
        mFragment = CreateCommentFragment.getInstance(timelineModelId);
        mTimelineUser = userModel;
        mIsLoadingMore = false;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
    }

    @Bindable
    public int getScrollPosition() {
        return mScrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        mScrollPosition = scrollPosition;
        notifyPropertyChanged(BR.scrollPosition);
    }

    @Override
    public void onGetCommentsSuccess(List<Comment> comments) {
        mOnEndScrollListener.setIsFetchingData(false);
        mAdapter.updateData(comments);
        setLoadingMore(false);
    }

    @Override
    public void onGetCommentsFailure(String message) {
        mOnEndScrollListener.setIsFetchingData(false);
        setLoadingMore(false);
    }

    @Override
    public void onGetCommentSuccess(Comment comment) {

    }

    @Override
    public void onAddComment(Comment comment) {
        if (comment == null) {
            return;
        }
        mAdapter.addComment(comment);
    }

    @Override
    public void onDeleteComment(Comment comment) {
        if (comment == null) {
            return;
        }
        mAdapter.deleteComment(comment);
    }

    @Override
    public void onUpdateComment(Comment comment) {
        if (comment == null) {
            return;
        }
        mAdapter.updateComment(comment);
    }

    @Override
    public void showPopupMenuComment(final Comment comment) {
        PopupMenu popupMenu;
        popupMenu = new PopupMenu(mContext, mViewGroupComment);
        popupMenu.inflate(R.menu.menu_item_comment);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.del_commnet:
                        showConfirmDeleteDialog(comment);
                        return true;
                    case R.id.edit_comment:
                        setFragment(EditCommentFragment.getInstance(comment));
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void deleComentSuccess(Comment comment) {
        mProgressDialog.cancel();
        onDeleteComment(comment);
    }

    private void showConfirmDeleteDialog(final Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.title_confirm_delete_comment);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.title_cancel, null);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteComment(comment);
                    }
                });
        builder.show();
    }

    @Override
    public void showDialogComment() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(R.string.msg_delete_comment);
        mProgressDialog.show();
    }

    @Bindable
    public OnEndScrollListener getOnEndScrollListener() {
        return mOnEndScrollListener;
    }

    public void setOnEndScrollListener(OnEndScrollListener onEndScrollListener) {
        mOnEndScrollListener = onEndScrollListener;
        notifyPropertyChanged(BR.onEndScrollListener);
    }

    @Override
    public void onPause() {
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        mPresenter = presenter;
        setLoadingMore(true);
        mPresenter.fetchCommentData(null);
    }

    @Bindable
    public CommentAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public void setAdapter(CommentAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onEndScrolled() {
        setLoadingMore(true);
        mPresenter.fetchCommentData(mAdapter.getLastComment());
    }

    public void onCreateCommentTouched() {
      /*  Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_TIMELINE, mTimelineModelId);
        bundle.putParcelable(Constant.EXTRA_USER, mUserModel);
        mNavigator.startActivityForResult(CreateCommentFragment.class, bundle,
                Constant.RequestCode.POST_COMMENT);*/
    }

    @Bindable
    public int getDefaultHeight() {
        return mDefaultHeight;
    }

    public void setDefaultHeight(int defaultHeight) {
        mDefaultHeight = defaultHeight;
        notifyPropertyChanged(BR.defaultHeight);
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Bindable
    public FragmentManager getManager() {
        return mManager;
    }

    public void setManager(FragmentManager manager) {
        mManager = manager;
        notifyPropertyChanged(BR.manager);
    }

    @Bindable
    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
        notifyPropertyChanged(BR.fragment);
    }

    @Override
    public void onItemTimelineClick(Comment item) {
        mContext.startActivity(SelectedImageDetailActivity.getIntent(mContext,
                new ArrayList(Arrays.asList(item.getMediaModel())), 0));
    }

    @Override
    public void onItemUserNameClick(Comment item) {
        if (mTimelineUser != null && mTimelineUser.getId().equals(item.getCreateUser().getId())) {
            return;
        }
        mNavigator.startActivity(ProfileUserActivity.getInstance(mContext, item.getCreateUser()));
    }

    @Override
    public boolean onItemLongClick(View viewGroup, final Comment comment) {
        mViewGroupComment = viewGroup;
        mPresenter.initDialogItem(comment);
        return true;
    }

    @Override
    public void onItemOptionClick(Comment item) {

    }

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(com.android.databinding.library.baseAdapters.BR.loadingMore);
    }

    @Override
    public void replaceFragment(BaseFragment baseFragment) {
        setFragment(baseFragment);
    }

    @Override
    public void onCommmentSuccess() {
        // move position to first
        setScrollPosition(0);
    }
}
