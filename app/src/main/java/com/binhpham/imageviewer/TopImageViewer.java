package com.binhpham.imageviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class TopImageViewer extends FrameLayout {
    private OnClickListener onClickListener;

    public TopImageViewer(Context context, OnClickListener onClickListener){
        super(context);
        this.onClickListener = onClickListener;
        init();
    }

    public TopImageViewer(@NonNull Context context) {
        super(context);
        init();
    }

    public TopImageViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopImageViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.top_image_viewer, this);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);
    }
}
