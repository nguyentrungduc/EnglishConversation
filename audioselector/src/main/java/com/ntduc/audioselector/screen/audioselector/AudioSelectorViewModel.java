package com.ntduc.audioselector.screen.audioselector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.ntduc.audioselector.BaseRecyclerViewAdapter;
import com.ntduc.audioselector.R;
import com.ntduc.audioselector.data.model.Audio;
import com.ntduc.audioselector.data.source.AudioDataSource;
import com.ntduc.audioselector.data.source.AudioRepository;
import com.ntduc.audioselector.data.source.callback.OnGetDataCallback;
import com.ntduc.audioselector.data.source.local.AudioLocalDataSource;
import com.ntduc.audioselector.util.Constant;
import com.ntduc.audioselector.util.loader.PlayerLoader;
import com.ntduc.audioselector.widget.dialog.recording.RecordingDialog;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class AudioSelectorViewModel implements AudioSelectorContract.ViewModel,
        OnGetDataCallback<List<Audio>>,
        BaseRecyclerViewAdapter.OnItemClickListener<Audio>, ItemAudioViewModel.OnItemCheckChange {

    private static final int LIMIT_CHOOSE_ITEM = 1;
    private static final String PREFIX_RECORDING = "RecordingAudio_";

    private int mSelectedCount;
    private AudioSelectorActivity mActivity;
    private AudioSelectorAdapter mAdapter;
    private PlayerLoader mPlayerLoader;
    private AudioSelectorContract.Presenter mPresenter;

    public AudioSelectorViewModel(AudioSelectorActivity activity) {
        mActivity = activity;
        AudioDataSource.LocalDataSource localDataSource = new AudioLocalDataSource(activity);
        AudioRepository audioRepository = new AudioRepository(localDataSource);
        audioRepository.getLocalAudios(this);
        mPlayerLoader = new PlayerLoader(mActivity);
    }

    @Override
    public void onGetDataSuccess(List<Audio> data) {
        mAdapter = new AudioSelectorAdapter(mActivity, data);
        mAdapter.setListener(this);
        mAdapter.setCheckChange(this);
    }

    @Override
    public void onGetDataFailed() {
        // No ops
    }

    @Override
    public void onStart() {
        if (Util.SDK_INT > Build.VERSION_CODES.M && mAdapter != null) {
            mPlayerLoader.initPlayer();
        }
    }

    @Override
    public void onResume() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M && mAdapter != null) {
            mPlayerLoader.initPlayer();
        }
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            mPlayerLoader.releasePlayer();
        }
    }

    @Override
    public void onStop() {
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            mPlayerLoader.releasePlayer();
        }
    }

    @Override
    public void setPresenter(AudioSelectorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public AudioSelectorAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onItemClick(View view, Audio data, int position) {
        mPlayerLoader.play(Uri.parse(data.getPath()));
    }

    @Override
    public void onCheckChange(Audio audio) {
        if (mSelectedCount >= LIMIT_CHOOSE_ITEM && !audio.isSelected()) {
            Toast.makeText(mActivity,
                    String.format(mActivity.getString(R.string.limit_audio), LIMIT_CHOOSE_ITEM),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        audio.setSelected(!audio.isSelected());
        if (audio.isSelected()) {
            mSelectedCount++;
        } else {
            mSelectedCount--;
        }
        mActivity.onItemAudioClick(mSelectedCount);
    }

    public void clearCheck() {
        mSelectedCount = 0;
        mAdapter.clearCheck();
    }

    public void finishActivity() {
        if (mAdapter.getSelectedAudios() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(AudioSelectorActivity.EXTRA_AUDIO,
                mAdapter.getSelectedAudios());
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }

    public void onCreateRecordingClick() {
        if (mActivity.getExternalCacheDir() == null) {
            return;
        }
        String fileName =
                PREFIX_RECORDING + System.currentTimeMillis() + Constant.RECORDING_FILE_FORMAT;
        String filePath = mActivity.getExternalCacheDir().getAbsolutePath() + "/" + fileName;
        RecordingDialog recordingDialog = RecordingDialog.getInstance(filePath, fileName);
        RecordingDialog.OnDismissRecordingClickListener listener =
                new RecordingDialog.OnDismissRecordingClickListener() {
                    @Override
                    public void onStore(String fileName, String filePath) {
                        String id = UUID.randomUUID().toString();
                        Audio audio = new Audio(id, fileName, filePath, true);
                        ArrayList<Audio> result = new ArrayList<>();
                        result.add(audio);
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra(AudioSelectorActivity.EXTRA_AUDIO,
                                result);
                        mActivity.setResult(Activity.RESULT_OK, intent);
                        mActivity.finish();
                    }

                    @Override
                    public void onCancel() {
                        // No ops
                    }
                };
        recordingDialog.setListener(listener);
        recordingDialog.show(
                mActivity.getSupportFragmentManager(), RecordingDialog.class.getSimpleName());
    }
}
