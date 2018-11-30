package com.ntduc.englishconversation.screen.register;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
import com.ntduc.englishconversation.BaseActivity;
import com.ntduc.englishconversation.R;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.ntduc.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.ntduc.englishconversation.databinding.ActivityRegisterBinding;
import com.ntduc.englishconversation.utils.navigator.Navigator;

/**
 * Register Screen.
 */
public class RegisterActivity extends BaseActivity {

    private RegisterContract.ViewModel mViewModel;

    public static Intent getInstance(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new RegisterViewModel(this, new Navigator(this));

        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());

        RegisterContract.Presenter presenter = new RegisterPresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);

        ActivityRegisterBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setViewModel((RegisterViewModel) mViewModel);
        getSupportActionBar(R.string.action_register);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
