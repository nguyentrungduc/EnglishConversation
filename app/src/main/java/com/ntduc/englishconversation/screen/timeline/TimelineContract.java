package com.ntduc.englishconversation.screen.timeline;

import com.ntduc.englishconversation.BasePresenter;
import com.ntduc.englishconversation.BaseViewModel;
import com.ntduc.englishconversation.data.model.Setting;
import com.ntduc.englishconversation.data.model.TimelineModel;
import com.ntduc.englishconversation.data.model.UserModel;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface TimelineContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetUserSuccess(UserModel data);

        void onCreateNewPostClick();

        void onGetTimelinesSuccess(List<TimelineModel> timelines);

        void onGetTimelinesFailure(String message);

        void onDestroy();

        void onGetSettingSuccess(Setting setting);

        void setTimelineUser(UserModel timelineUser);

        void onAddTimeline(TimelineModel value);

        void onEditTimeline(TimelineModel value);

        void onDeleteTimeline(TimelineModel value);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onDestroy();

        void getTimelineData(TimelineModel lastTimelineModel);
    }
}
