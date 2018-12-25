package com.ntduc.englishconversation.screen.createPost;

import android.support.annotation.IntDef;

import static com.ntduc.englishconversation.screen.createPost.AdapterType.CONVERSATION;
import static com.ntduc.englishconversation.screen.createPost.AdapterType.MEDIA;



@IntDef({MEDIA, CONVERSATION})
public @interface AdapterType {
    int MEDIA = 0;
    int CONVERSATION = 1;
}
