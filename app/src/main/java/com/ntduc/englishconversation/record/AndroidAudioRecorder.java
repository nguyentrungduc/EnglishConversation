package com.ntduc.englishconversation.record;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.Fragment;
import com.ntduc.englishconversation.record.model.AudioChannel;
import com.ntduc.englishconversation.record.model.AudioSampleRate;
import com.ntduc.englishconversation.record.model.AudioSource;

public class AndroidAudioRecorder {

    public static final String EXTRA_FILE_PATH = "filePath";
    public static final String EXTRA_FILE_NAME = "fileName";
    protected static final String EXTRA_COLOR = "color";
    protected static final String EXTRA_SOURCE = "source";
    protected static final String EXTRA_CHANNEL = "channel";
    protected static final String EXTRA_SAMPLE_RATE = "sampleRate";
    protected static final String EXTRA_AUTO_START = "autoStart";
    protected static final String EXTRA_KEEP_DISPLAY_ON = "keepDisplayOn";

    private Activity activity;
    private Fragment fragment;

    private String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
    private String fileName = "recorded_audio.wav";
    private AudioSource source = AudioSource.MIC;
    private AudioChannel channel = AudioChannel.STEREO;
    private AudioSampleRate sampleRate = AudioSampleRate.HZ_44100;
    private int color = Color.parseColor("#546E7A");
    private int requestCode = 0;
    private boolean autoStart = false;
    private boolean keepDisplayOn = false;

    private AndroidAudioRecorder(Activity activity) {
        this.activity = activity;
    }

    private AndroidAudioRecorder(Fragment fragment) {
        this.fragment = fragment;
    }

    public static AndroidAudioRecorder with(Activity activity) {
        return new AndroidAudioRecorder(activity);
    }

    public static AndroidAudioRecorder with(Fragment fragment) {
        return new AndroidAudioRecorder(fragment);
    }

    public AndroidAudioRecorder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
    public AndroidAudioRecorder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public AndroidAudioRecorder setColor(int color) {
        this.color = color;
        return this;
    }

    public AndroidAudioRecorder setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public AndroidAudioRecorder setSource(AudioSource source) {
        this.source = source;
        return this;
    }

    public AndroidAudioRecorder setChannel(AudioChannel channel) {
        this.channel = channel;
        return this;
    }

    public AndroidAudioRecorder setSampleRate(AudioSampleRate sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public AndroidAudioRecorder setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        return this;
    }

    public AndroidAudioRecorder setKeepDisplayOn(boolean keepDisplayOn) {
        this.keepDisplayOn = keepDisplayOn;
        return this;
    }

    public void record() {
        Intent intent = new Intent(activity, AudioRecorderActivity.class);
        intent.putExtra(EXTRA_FILE_PATH, filePath);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        intent.putExtra(EXTRA_COLOR, color);
        intent.putExtra(EXTRA_SOURCE, source);
        intent.putExtra(EXTRA_CHANNEL, channel);
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate);
        intent.putExtra(EXTRA_AUTO_START, autoStart);
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn);
        activity.startActivityForResult(intent, requestCode);
    }

    public void recordFromFragment() {
        Intent intent = new Intent(fragment.getActivity(), AudioRecorderActivity.class);
        intent.putExtra(EXTRA_FILE_PATH, filePath);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        intent.putExtra(EXTRA_COLOR, color);
        intent.putExtra(EXTRA_SOURCE, source);
        intent.putExtra(EXTRA_CHANNEL, channel);
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate);
        intent.putExtra(EXTRA_AUTO_START, autoStart);
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn);
        fragment.startActivityForResult(intent, requestCode);
    }


}
