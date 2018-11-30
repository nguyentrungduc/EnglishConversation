package com.ntduc.englishconversation.screen.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.ntduc.englishconversation.BaseActivity;
import com.ntduc.englishconversation.R;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.ntduc.englishconversation.databinding.ActivityMainBinding;
import com.ntduc.englishconversation.utils.navigator.Navigator;

/**
 * Main Screen.
 */
public class MainActivity extends BaseActivity {

    public static final int NEW_POSITION = 0;
    public static final int TOP_VOTED_POSITION = 1;
    public static final int YOUR_POST_POSITION = 2;
    public static final int SETTING_POSITION = 3;
    private static final int PRELOAD_SCREEN_NUMBER = 3;

    private MainContract.ViewModel mViewModel;
    private ViewPager mViewPager;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new MainViewModel(this, new Navigator(this));
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        MainContract.Presenter presenter = new MainPresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel((MainViewModel) mViewModel);
        mViewPager = binding.viewPager;
        mViewPager.setOffscreenPageLimit(PRELOAD_SCREEN_NUMBER);
        TabLayout tabLayout = binding.tabLayout;
        if (tabLayout.getTabAt(NEW_POSITION).getIcon() != null) {
            int selectedColor = ContextCompat.getColor(this, R.color.light_blue_900);
            tabLayout.getTabAt(NEW_POSITION).getIcon().setColorFilter(selectedColor,
                    PorterDuff.Mode.SRC_ATOP);
        }
        getSupportActionBar().hide();
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

    public void positionComponents(int position) {
        mViewPager.setCurrentItem(
                position,
                true);
    }
}
