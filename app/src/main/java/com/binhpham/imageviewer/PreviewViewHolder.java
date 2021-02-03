package com.binhpham.imageviewer;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.mrkaz.imageviewer.ImageViewer;
import com.mrkaz.imageviewer.loader.ImageLoader;
import com.mrkaz.imageviewer.viewer.view.AdvanceImageView;
import com.mrkaz.imageviewer.viewer.viewholder.DefaultViewHolder;

public class PreviewViewHolder<T> extends DefaultViewHolder<T> implements LifecycleObserver {
    private final ImageView imageView;
    private final View videoView;
    private final ImageLoader<String> imageLoader;
    private boolean active = false;
    private boolean hasVideo;

    public static PreviewViewHolder<String> build(ImageView imageView, ImageLoader<String> imageLoader) {
        FrameLayout parent = (FrameLayout) LayoutInflater.from(imageView.getContext()).inflate(R.layout.image_viewer_video, null);
        parent.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout viewImage = parent.findViewById(R.id.view_image);
        viewImage.addView(imageView);
        View videoView = parent.findViewById(R.id.player);
        return new PreviewViewHolder<>(parent, imageView, videoView, imageLoader);
    }

    private PreviewViewHolder(View parentView, ImageView iv, View vv, ImageLoader<String> imageLoader) {
        super(parentView);
        imageView = iv;
        videoView = vv;
        this.imageLoader = imageLoader;
        if (imageView instanceof AdvanceImageView) {
            ((AdvanceImageView) imageView).addLifeCycle(this);
        }
    }

    private boolean isVideo(String url) {
        return false;
    }

    @Override
    public void bind(int position, T uri) {
        String image = uri.toString();
        videoView.setTag(new VideoPayload(position, image));
        if (isVideo(image)) {
            hasVideo = true;
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
        } else {
            imageLoader.loadImage(imageView, image);
            hasVideo = false;
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDialogClosed() {
        videoView.postDelayed(() -> {
            videoView.setVisibility(View.GONE);
            //stop video
        }, 10);
    }

    @Override
    public void setIsVisible(boolean isVisible) {
        if (isVisible == active)
            return;

        active = isVisible;
        if (!hasVideo)
            return;

        VideoPayload payload = (VideoPayload) videoView.getTag();
        if (isVisible) {
            //play video
        } else {
            //stop video
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void startVideo() {
        if (hasVideo) {
            //play video
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pauseVideo() {
        if (hasVideo) {
            //stop video
        }
    }

    private static class VideoPayload {
        int position = 0;
        String source = "";

        VideoPayload(int position, String source) {
            this.position = position;
            this.source = source;
        }
    }


}