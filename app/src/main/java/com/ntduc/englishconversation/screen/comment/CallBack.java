package com.ntduc.englishconversation.screen.comment;

import com.ntduc.englishconversation.BaseFragment;

/**
 * Created by Bui Danh Nam on 31/1/2018.
 */

public interface CallBack {
    void replaceFragment(BaseFragment baseFragment);

    //  notifi parent fragment PostCommentSuccess
    void onPostCommentSuccess();
}
