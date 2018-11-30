package com.ntduc.englishconversation.screen.createPost;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntduc.englishconversation.R;
import com.ntduc.englishconversation.data.model.MediaModel;
import com.ntduc.englishconversation.databinding.ItemAudioBinding;
import com.ntduc.englishconversation.databinding.ItemImageBinding;
import com.ntduc.englishconversation.databinding.ItemVideoBinding;
import com.ntduc.englishconversation.screen.basePost.BasePostViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the CreatePost screen.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.BaseViewHolder> {

    private List<MediaModel> mMediaModels;
    private BasePostViewModel mViewModel;

    public MediaAdapter(BasePostViewModel viewModel) {
        mViewModel = viewModel;
        mMediaModels = new ArrayList<>();
    }

    public void setViewModel(BasePostViewModel viewModel) {
        mViewModel = viewModel;
    }

    private void updateData(List<MediaModel> mediaModels) {
        if (mediaModels == null) {
            return;
        }
        mMediaModels.addAll(mediaModels);
        notifyDataSetChanged();
    }

    public void setData(List<MediaModel> mediaModels) {
        mMediaModels.clear();
        updateData(mediaModels);
    }

    public List<MediaModel> getData() {
        return mMediaModels;
    }

    public void removeItem(MediaModel mediaModel) {
        mMediaModels.remove(mediaModel);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mMediaModels.get(position).getType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.IMAGE:
                ItemImageBinding imageBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_image,
                        parent,
                        false);
                return new ImageViewHolder(imageBinding);
            case MediaModel.MediaType.AUDIO:
                ItemAudioBinding audioBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_audio,
                        parent,
                        false);
                return new AudioViewHolder(audioBinding);
            case MediaModel.MediaType.VIDEO:
                ItemVideoBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_video,
                        parent,
                        false);
                return new VideoViewHolder(binding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(mMediaModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mMediaModels != null ? mMediaModels.size() : 0;
    }

    /**
     * Viewholder to display video
     */
    public class VideoViewHolder extends BaseViewHolder {
        private ItemVideoBinding mVideoBinding;

        VideoViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            mVideoBinding = binding;
        }

        @Override
        public void bindData(MediaModel video) {
            mVideoBinding.setMediaModel(video);
            mVideoBinding.setViewModel(mViewModel);
            mVideoBinding.executePendingBindings();
        }
    }

    /**
     * Viewholder to display image
     */
    public class ImageViewHolder extends BaseViewHolder {
        private ItemImageBinding mImageBinding;

        ImageViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            mImageBinding = binding;
        }

        @Override
        public void bindData(MediaModel video) {
            mImageBinding.setMediaModel(video);
            mImageBinding.setViewModel(mViewModel);
            mImageBinding.executePendingBindings();
        }
    }

    /**
     * Viewholder to display audio
     */
    public class AudioViewHolder extends BaseViewHolder {
        private ItemAudioBinding mImageBinding;

        AudioViewHolder(ItemAudioBinding binding) {
            super(binding.getRoot());
            mImageBinding = binding;
        }

        @Override
        public void bindData(MediaModel video) {
            mImageBinding.setRecord(video);
            mImageBinding.setViewModel(mViewModel);
            mImageBinding.executePendingBindings();
        }
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(MediaModel video);
    }


}
