package com.example.artproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    // Конструктор и методы для управления данными постов
    private List<Post> postsList;
    public PostsAdapter(List<Post> postsList) {
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.authorTextView.setText(post.getAuthor());
        holder.postTitle.setText(post.getTitle());
        Glide.with(holder.itemView.getContext()).load(post.getImageUrl()).into(holder.postImage);
        holder.postDescription.setText(post.getDescription());
        holder.postTime.setText(post.getCreationTime());



        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post currentPost = postsList.get(holder.getAdapterPosition());
                currentPost.setLiked(!currentPost.isLiked()); // Переключаем состояние лайка
                holder.likeImageView.setImageResource(currentPost.isLiked() ? R.drawable.ic_like_active : R.drawable.ic_like);
                // Здесь можно добавить код для обновления состояния лайка в базе данных, если необходимо
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView postTitle;
        ImageView likeImageView;
        ImageView postImage;
        TextView postDescription;
        TextView postTime;
        ImageView likeIcon;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.post_author);
            postTitle = itemView.findViewById(R.id.post_title);
            postImage = itemView.findViewById(R.id.post_image);
            postDescription = itemView.findViewById(R.id.post_description);
            postTime = itemView.findViewById(R.id.post_time);
            likeImageView = itemView.findViewById(R.id.likeImageView);;
        }
    }



}