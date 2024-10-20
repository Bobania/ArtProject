package com.example.artproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPhotosActivity extends AppCompatActivity implements PhotosAdapter.OnImageClickListener {
    private RecyclerView photosRecyclerView;
    private PhotosAdapter photoAdapter;
    private List<String> photoUrls = new ArrayList<>();
    String EmailMyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photos);

        // Получение EmailMyAccount из глобального класса или Intent
        EmailMyAccount = ((GlobalVars) getApplicationContext()).getEmailMyAccount();
        if (EmailMyAccount == null || EmailMyAccount.isEmpty()) {
            EmailMyAccount = getIntent().getStringExtra("Email");
        }

        // Проверка на null и пустую строку
        if (EmailMyAccount == null || EmailMyAccount.isEmpty()) {
            // Обработка ошибки, EmailMyAccount не должен быть null или пустым
            return;
        }

        // Преобразование email в формат, совместимый с Firebase ключами
        String safeEmail = EmailMyAccount.replace('.', ',');

        photosRecyclerView = findViewById(R.id.photosRecyclerView);
        photoAdapter = new PhotosAdapter(this, photoUrls, this);
        photosRecyclerView.setAdapter(photoAdapter);
        photosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Обновление ссылки на базу данных с учетом структуры MyAccount
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtUsers");
        databaseReference.child(safeEmail).child("userPhotos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photoUrls.clear(); // Очистка списка перед добавлением новых элементов
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String photoUrl = snapshot.getValue(String.class);
                    photoUrls.add(photoUrl);
                }
                photoAdapter.notifyDataSetChanged(); // Обновление адаптера
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок, например, вывод сообщения об ошибке
            }
        });
    }

    @Override
    public void onImageClick(String imageUrl) {
        // Пользователь выбрал изображение, устанавливаем результат и закрываем активити
        Intent returnIntent = new Intent();
        returnIntent.putExtra("selectedImage", imageUrl);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onImageLongClick(String imageUrl) {
        // Можно добавить дополнительные действия при долгом нажатии, если это необходимо
    }
}