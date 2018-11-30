package com.ntduc.englishconversation.screen.videoDetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
import com.ntduc.englishconversation.BaseActivity;
import com.ntduc.englishconversation.R;
import com.ntduc.englishconversation.data.model.TimelineModel;
import com.ntduc.englishconversation.data.model.UserModel;
import com.ntduc.englishconversation.databinding.ActivityVideoDetailBinding;
import com.ntduc.englishconversation.utils.Constant;

import static com.ntduc.englishconversation.utils.Constant.EXTRA_USER;

/**
 * VideoDetail Screen.
 */
public class VideoDetailActivity extends BaseActivity {

    private VideoDetailContract.ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimelineModel model = getIntent().getExtras().getParcelable(Constant.EXTRA_TIMELINE);
        UserModel userModel = getIntent().getExtras().getParcelable(EXTRA_USER);
        mViewModel = new VideoDetailViewModel(this, getSupportFragmentManager(), model, userModel);
        VideoDetailContract.Presenter presenter = new VideoDetailPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityVideoDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        binding.setViewModel((VideoDetailViewModel) mViewModel);
        getSupportActionBar(
                getString(R.string.title_post_detail, model.getCreatedUser().getUserName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mViewModel.finishActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mViewModel.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mViewModel.onResume();
        super.onResume();
    }
}
