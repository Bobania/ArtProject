package com.example.artproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections; // Импортируем Collections
import java.util.List;

public class HomePageGallery extends AppCompatActivity {
    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private ArrayList<Post> postsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_gallery);

        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsList = new ArrayList<>();
        postsAdapter = new PostsAdapter(postsList);
        postsRecyclerView.setAdapter(postsAdapter);

        // Получение ссылки на базу данных
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Слушатель для получения данных из Firebase
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postsList.add(post);
                }
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomePageGallery.this, "Ошибка загрузки постов: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}