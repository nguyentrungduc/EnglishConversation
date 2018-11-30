package com.ntduc.englishconversation.screen.timeline;

import android.view.View;

/**
 * Created by VinhTL on 21/12/2017.
 */

public interface OnTimelineItemTouchListener<T> {
    void onItemTimelineClick(T item);

    void onItemUserNameClick(T item);

    boolean onItemLongClick(View view, T item);

    void onItemOptionClick(T item);

}
