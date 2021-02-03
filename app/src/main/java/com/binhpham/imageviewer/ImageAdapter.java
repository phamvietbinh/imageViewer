package com.binhpham.imageviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mrkaz.imageviewer.ImageViewer;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by apple on 3/24/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    public interface ImageItemCallback {
        void onItemDelete(int index, String url);
    }

    private final Context context;
    private final List<String> urls;
    private ImageItemCallback imageItemCallback;
    private RecyclerView recyclerView;
    private ImageViewer<String> imagePreviewer;

    public ImageAdapter(Context context, List<String> urls, RecyclerView recyclerView) {
        this.context = context;
        this.urls = urls;
        this.recyclerView = recyclerView;
    }

    public void setImageItemCallback(ImageItemCallback imageItemCallback) {
        this.imageItemCallback = imageItemCallback;
    }

    public void addImageUrl(String url) {
        if (url != null) {
            urls.add(url);
            notifyItemInserted(urls.size());
        }
    }

    public void addImageUrl(List<String> url) {
        if (url != null) {
            urls.addAll(url);
            notifyDataSetChanged();
        }
    }

    public List<String> getImageUrl() {
        return urls;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_viewer, parent, false));
    }

    private boolean isVideo(String url) {
        return false;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        if (isVideo(urls.get(position))) {
            holder.playIcon.setVisibility(View.VISIBLE);
        } else {
            holder.playIcon.setVisibility(View.GONE);
        }
        try {
            Glide.with(holder.itemView)
                    .load(new File(urls.get(position)).isFile() ?
                            new File(urls.get(position)) : new URL(urls.get(position)))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_baseline_image_24)
                            .override(200, 200)
                            .dontTransform())
                    .into(holder.imgContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        public ImageView imgContent;
        public CardView viewItem;
        CardView playIcon;

        public ImageHolder(View itemView) {
            super(itemView);
            imgContent = itemView.findViewById(R.id.img_content);
            viewItem = itemView.findViewById(R.id.view_item);
            playIcon = itemView.findViewById(R.id.play_icon);
            itemView.findViewById(R.id.btn_delete).setOnClickListener(view -> {
                if (imageItemCallback != null) {
                    imageItemCallback.onItemDelete(getAdapterPosition(), urls.get(getAdapterPosition()));
                }
                urls.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
            itemView.findViewById(R.id.img_content).setOnClickListener(view -> {
                int position = getAdapterPosition();
                imagePreviewer =
                        new ImageViewer.Builder<>
                                (
                                        context,
                                        urls,
                                        (imageView, image) -> Glide.with(imageView)
                                                .load(new File(image).isFile() ? new File(image) : image)
                                                .thumbnail(Glide.with(context).load(R.drawable.ic_baseline_image_24))
                                                .apply(new RequestOptions().fitCenter().error(R.drawable.ic_baseline_image_24))
                                                .into(imageView), PreviewViewHolder::build
                                )
                                .withImageChangeListener(newPosition -> {
                                    recyclerView.scrollToPosition(newPosition);
                                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(newPosition);
                                    if (holder instanceof ImageAdapter.ImageHolder) {
                                        ImageAdapter.ImageHolder mediaViewHolder = (ImageAdapter.ImageHolder) holder;
                                        imagePreviewer.updateTransitionImage(mediaViewHolder.imgContent);
                                    }
                                })
                                .withOverlayView(new TopImageViewer(context, view1 -> {
                                    imagePreviewer.dismiss();
                                }))
                                .withHiddenStatusBar(false)
                                .withStartPosition(position)
                                .withTransitionFrom(imgContent)
                                .show();
            });
        }
    }

}
