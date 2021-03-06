package com.ntduc.englishconversation.screen.timeline.hometimeline;

import com.ntduc.englishconversation.data.model.GenericsModel;
import com.ntduc.englishconversation.data.model.Status;
import com.ntduc.englishconversation.data.model.TimelineModel;
import com.ntduc.englishconversation.data.source.SettingRepository;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.ntduc.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.ntduc.englishconversation.screen.timeline.TimelineContract;
import com.ntduc.englishconversation.screen.timeline.TimelinePresenter;
import com.ntduc.englishconversation.screen.timeline.TimelineViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeTimelinePresenter extends TimelinePresenter {


    public HomeTimelinePresenter(TimelineContract.ViewModel viewModel,
                                 AuthenicationRepository authenicationRepository,
                                 TimelineRepository timelineRepository,
                                 SettingRepository settingRepository) {
        super(viewModel, authenicationRepository, timelineRepository, settingRepository);
    }

    /**
     * allow user to create new post in home timeline
     */
    @Override
    protected void initAllowCreatePost() {
        ((TimelineViewModel) mViewModel).setAllowCreatePost(true);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getTimelineData(TimelineModel lastTimelineModel) {
        Observable<List<TimelineModel>> observable =
                mTimelineRepository.getTimeline(lastTimelineModel);

        mDisposable.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<TimelineModel>>() {
                    @Override
                    public void onNext(List<TimelineModel> timelineModels) {
                        mViewModel.onGetTimelinesSuccess(timelineModels);

                        mLastTimelineModel = timelineModels.isEmpty() ? null
                                        : timelineModels.get(timelineModels.size() - 1);
                        mTimelineRepository.removeListener();
                        registerModifyTimelines(mLastTimelineModel);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetTimelinesFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    public void registerModifyTimelines(TimelineModel timelineModel) {
        Observable<GenericsModel<Integer, TimelineModel>> observable = mTimelineRepository
                .registerModifyTimelines(timelineModel);

        Disposable disposable = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<GenericsModel<Integer, TimelineModel>>() {
                    @Override
                    public void onNext(GenericsModel<Integer, TimelineModel> genericsModel) {
                        if (mLastTimelineModel == null) {
                            mLastTimelineModel = genericsModel.getValue();
                        }
                        if (genericsModel.getKey() == Status.ADD) {
                            mViewModel.onAddTimeline(genericsModel.getValue());
                        }
                        if (genericsModel.getKey() == Status.EDIT) {
                            mViewModel.onEditTimeline(genericsModel.getValue());
                        }
                        if (genericsModel.getKey() == Status.DELETE) {
                            mViewModel.onDeleteTimeline(genericsModel.getValue());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetTimelinesFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposable.add(disposable);
    }
}
