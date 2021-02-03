package com.mrkaz.imageviewer.viewer.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import com.github.chrisbanes.photoview.PhotoView;

public class AdvanceImageView extends PhotoView {
    public AdvanceImageView(Context context) {
        super(context);
    }

    public AdvanceImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public AdvanceImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public void addLifeCycle(LifecycleObserver lifecycleObserver) {
        if (getContext() instanceof AppCompatActivity) {
            ((AppCompatActivity) getContext()).getLifecycle().addObserver(lifecycleObserver);
        }
    }
}
