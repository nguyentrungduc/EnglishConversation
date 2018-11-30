package com.ntduc.englishconversation.screen.editPost;

import android.app.Activity;

import com.ntduc.englishconversation.data.model.TimelineModel;
import com.ntduc.englishconversation.screen.basePost.BasePostViewModel;
import com.ntduc.englishconversation.utils.navigator.Navigator;

/**
 * Created by Sony on 2/2/2018.
 */

public class EditPostViewModel extends BasePostViewModel implements EditPostContract.ViewModel{

    public EditPostViewModel(Activity activity, Navigator navigator) {
        super(activity, navigator);
    }

    public EditPostViewModel(Activity activity, Navigator navigator, TimelineModel timelineModel) {
        super(activity, navigator, timelineModel);
    }

}
