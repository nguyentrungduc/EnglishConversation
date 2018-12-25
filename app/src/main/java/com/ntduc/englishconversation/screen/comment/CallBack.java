package com.ntduc.englishconversation.screen.comment;

import com.ntduc.englishconversation.BaseFragment;


public interface CallBack {
    void replaceFragment(BaseFragment baseFragment);

    //  notifi parent fragment PostCommentSuccess
    void onPostCommentSuccess();
}
