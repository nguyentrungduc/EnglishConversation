package com.ntduc.englishconversation.screen;

/**
 * Created by doan.van.toan on 1/5/18.
 */

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

/**
 * Base media viewholder
 */

public abstract class BaseMediaViewHolder<T> extends BaseViewHolder<T>
        implements ToroPlayer {
    private static final float VISIBLE_ARE_TIMELINE_ADAPTER = 0.85f;

    protected CustomSimpleExoPlayerHelper mHelper;
    protected Uri mUri;

    protected abstract SimpleExoPlayerView getMediaPlayerView();

    public BaseMediaViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMediaPlayerView().setUseController(true);
            }
        });
    }

    @NonNull
    @Override
    public View getPlayerView() {
        return getMediaPlayerView();
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        return mHelper != null ? mHelper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @Nullable PlaybackInfo playbackInfo) {
        if (mHelper == null) {
            mHelper = new CustomSimpleExoPlayerHelper(container, this, mUri);
        }
        mHelper.initialize(playbackInfo);
    }

    @Override
    public void play() {
        if (mHelper != null) {
            mHelper.play();
        }
    }

    @Override
    public void pause() {
        if (mHelper != null) {
            mHelper.pause();
        }
    }

    @Override
    public boolean isPlaying() {
        return mHelper != null && mHelper.isPlaying();
    }

    @Override
    public void release() {
        if (mHelper != null) {
            mHelper.release();
            mHelper = null;
        }
    }

    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this,
                itemView.getParent()) >= VISIBLE_ARE_TIMELINE_ADAPTER;
    }

    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }
}