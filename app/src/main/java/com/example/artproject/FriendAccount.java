package com.example.artproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FriendAccount extends AppCompatActivity implements FriendPhotosAdapter.OnImageClickListener {
    List<Integer> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_account);

        RecyclerView recyclerView = findViewById(R.id.friendphotosRecyclerView);

        imageList.add(R.drawable.penguinart1);
        imageList.add(R.drawable.penguinart2);

        FriendPhotosAdapter adapter = new FriendPhotosAdapter(this, imageList);
        adapter.setOnImageClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);


    }
    @Override
    public void onImageClick(int position) {

        int imageResource = imageList.get(position);
        // Открываем картинку в полном размере

        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
        dialog.setContentView(R.layout.dialog_image);

        ImageView imageView = dialog.findViewById(R.id.dialogImageView);
        imageView.setImageResource(imageResource);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

// Показываем диалоговое окно
        dialog.show();
        // Можно использовать диалоговое окно или другой подход
    }

    @Override
    public void onBackPressed() {
        // Переопределяем метод onBackPressed
        super.onBackPressed();
        // Этот вызов 'super.onBackPressed()' будет обрабатывать возвращение к предыдущей активности в стеке
    }
    public void onClick(View view) {
        // Проверяем, что нажатие пришло от кнопки с id backbuttonmess
        if (view.getId() == R.id.backbuttonfriend) {
            // Имитируем нажатие системной кнопки "назад"
            onBackPressed();
        }
    }

}