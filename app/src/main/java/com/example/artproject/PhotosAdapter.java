package com.example.artproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Context context;
    private List<String> photoUrls;

    private OnImageClickListener listener;
    public PhotosAdapter(Context context, List<String> photoUrls) {
        this.context = context;
        this.photoUrls = photoUrls;
    }

    public PhotosAdapter(Context context, List<String> photoUrls, OnImageClickListener listener) {
        this.context = context;
        this.photoUrls = photoUrls;
        this.listener = listener; // Установка слушателя
    }

    public interface OnImageClickListener {
        void onImageClick(String imageUrl);
        void onImageLongClick(String imageUrl);
    }




    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoUrl = photoUrls.get(position);
        Glide.with(context).load(photoUrl).into(holder.photoImageView);


        // Обработчики событий для короткого и долгого нажатия
        holder.photoImageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageClick(photoUrl);
            }
        });

        holder.photoImageView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onImageLongClick(photoUrl);
            }
            return true;
        });


    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }



    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }
}