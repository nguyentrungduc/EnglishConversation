package com.ntduc.englishconversation.data.model;

import android.support.annotation.IntDef;

import static com.ntduc.englishconversation.data.model.Status.ADD;
import static com.ntduc.englishconversation.data.model.Status.DELETE;
import static com.ntduc.englishconversation.data.model.Status.EDIT;


/**
 * Description: status comment
 */

@IntDef({ADD, DELETE, EDIT})
public @interface Status {
    int ADD = 0;
    int DELETE = 1;
    int EDIT = 2;
}
