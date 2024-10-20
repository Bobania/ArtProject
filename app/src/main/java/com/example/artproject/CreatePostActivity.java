package com.example.artproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private TextView creationTimeTextView;
    private ImageView selectedImageView;
    private String selectedImageUrl; // Объявление переменной здесь
    private String EmailMyAccount;
    private String NameMyAccount;


    private Button selectImageButton, createPostButton;
    private static final int PICK_USER_PHOTO_REQUEST = 3;
    public static final int PICK_PHOTO_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        NameMyAccount = ((GlobalVars) getApplicationContext()).getNameMyAccount();
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        creationTimeTextView = findViewById(R.id.creationTimeTextView);
        selectedImageView = findViewById(R.id.selectedImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        createPostButton = findViewById(R.id.createPostButton);

        // Установите текущее время создания
        setCurrentTime();

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск UserPhotosActivity для выбора изображения
                Intent intent = new Intent(CreatePostActivity.this, UserPhotosActivity.class);
                intent.putExtra("Email", EmailMyAccount); // Передача email пользователя
                startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE);
            }
        });

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание объекта Post
                Post newPost = new Post(
                        NameMyAccount,
                        titleEditText.getText().toString(),
                        selectedImageUrl,
                        descriptionEditText.getText().toString(),
                        creationTimeTextView.getText().toString()
                );

                // Получение ссылки на базу данных
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // Добавление поста в базу данных
                databaseReference.child("posts").push().setValue(newPost)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Пост успешно добавлен в базу данных
                                Toast.makeText(CreatePostActivity.this, "Пост добавлен!", Toast.LENGTH_SHORT).show();
                                // Завершение активности и возвращение к предыдущей активности в стеке
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Ошибка при добавлении поста в базу данных
                                Toast.makeText(CreatePostActivity.this, "Ошибка при добавлении поста: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void setCurrentTime() {
        // Форматирование и установка текущего времени
        String currentTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        creationTimeTextView.setText(currentTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Получение URL изображения, выбранного в UserPhotosActivity
            selectedImageUrl = data.getStringExtra("selectedImage"); // Присваивание значения переменной
            // Загрузка изображения в ImageView с помощью Glide
            Glide.with(this).load(selectedImageUrl).into(selectedImageView);
        }
    }
}