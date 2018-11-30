package com.ntduc.videoselector.data.source;

import com.ntduc.videoselector.data.model.VideoModel;

import java.util.List;

/**
 * Created by doan.van.toan on 1/15/18.
 */

public class VideoRepository implements VideoDataSource {

    private VideoDataSource mLocalDataSource;

    public VideoRepository(VideoDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public void getVideos(Callback<List<VideoModel>> callback) {
        mLocalDataSource.getVideos(callback);
    }
}
