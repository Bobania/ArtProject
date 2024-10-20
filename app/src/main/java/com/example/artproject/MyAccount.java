package com.example.artproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccount extends AppCompatActivity implements PhotosAdapter.OnImageClickListener{
    CircleImageView avatarImageView;
    private List<String> photoUrls = new ArrayList<>();
    private PhotosAdapter photoAdapter;
    private RecyclerView photosRecyclerView;
    String EmailMyAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);


        photosRecyclerView = findViewById(R.id.photosRecyclerView);
        photoAdapter = new PhotosAdapter(this, photoUrls);
        photosRecyclerView.setAdapter(photoAdapter);


        String NameMyAccount = ((GlobalVars) getApplicationContext()).getNameMyAccount();
        TextView usernameText = findViewById(R.id.usernameText);
        usernameText.setText(NameMyAccount);
        EmailMyAccount = ((GlobalVars) getApplicationContext()).getEmailMyAccount();

        photoAdapter.setOnImageClickListener(this);

        int numberOfColumns = 3;
        photosRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtUsers");
        databaseReference.child(EmailMyAccount).child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Получение URL изображения из базы данных
                    String imageUrl = dataSnapshot.getValue(String.class);
                    // Загрузка изображения в ImageView с помощью Glide
                    Glide.with(MyAccount.this).load(imageUrl).into(avatarImageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyAccount.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // Получение URL изображений из Firebase и обновление адаптера

        databaseReference.child(EmailMyAccount).child("userPhotos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String photoUrl = snapshot.getValue(String.class);
                        photoUrls.add(photoUrl);
                    }
                    photoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyAccount.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

;

        // Обработка нажатия кнопки выхода
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание интента для перехода на активити Entry
                Intent intent = new Intent(MyAccount.this, Entry.class);
                startActivity(intent);

                // Закрытие текущего активити
                finish();
            }
        });
        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск выбора изображений для альбома
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение для альбома"), PICK_ALBUM_IMAGE_REQUEST);
            }
        });


        avatarImageView = findViewById(R.id.avatarImage);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск выбора изображения
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
            }
        });



        Button createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccount.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Выберите изображения"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onImageLongClick(String imageUrl) {
        // Создайте диалоговое окно для подтверждения удаления
        new AlertDialog.Builder(this)
                .setTitle("Удаление фотографии")
                .setMessage("Вы уверены, что хотите удалить эту фотографию?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeImageFromAlbum(imageUrl); // Вызов метода удаления
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    // Код для обработки результата выбора изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (requestCode == PICK_IMAGE_REQUEST) {
                // Загрузка изображения в Firebase Storage и обновление профиля пользователя
                uploadImageToFirebase(imageUri, EmailMyAccount);
            } else if (requestCode == PICK_ALBUM_IMAGE_REQUEST) {
                // Загрузка изображения в Firebase Storage и добавление в альбом пользователя
                uploadAlbumImageToFirebase(imageUri, EmailMyAccount);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, String userEmail) {
        // Ссылка на ваш Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://planetarium-ab9b2.appspot.com");

        // Создание ссылки на место в Storage, где будет храниться изображение
        StorageReference fileReference = storageReference.child("profileImages/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

        // Загрузка изображения в Firebase Storage
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Получение URL загруженного файла
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Полученный URL изображения
                                String imageUrl = uri.toString();

                                // Сохранение URL в Firebase Realtime Database
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtUsers");
                                databaseReference.child(userEmail).child("profileImageUrl").setValue(imageUrl);

                                // Обновление UI с новым изображением
                                Glide.with(MyAccount.this).load(imageUrl).into(avatarImageView);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadAlbumImageToFirebase(Uri imageUri, String userEmail) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://planetarium-ab9b2.appspot.com");

        // Создание ссылки на место в Storage, где будет храниться изображение альбома
        StorageReference fileReference = storageReference.child("userPhotos/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

        // Загрузка изображения в Firebase Storage
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Получение URL загруженного файла
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Полученный URL изображения
                                String imageUrl = uri.toString();

                                // Сохранение URL в Firebase Realtime Database
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtUsers");
                                String uploadId = databaseReference.child(userEmail).child("userPhotos").push().getKey();
                                databaseReference.child(userEmail).child("userPhotos").child(uploadId).setValue(imageUrl);

                                // Добавление URL в список и обновление адаптера
                                photoUrls.add(imageUrl);
                                photoAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onImageClick(String imageUrl) {
        // Откройте изображение в полном размере, например, в диалоговом окне:
        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
        dialog.setContentView(R.layout.dialog_image);


        ImageView imageView = dialog.findViewById(R.id.dialogImageView);
        Glide.with(this).load(imageUrl).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();



    }

    public void removeImageFromAlbum(String imageUrl) {
        // Удаление URL из списка
        photoUrls.remove(imageUrl);
        // Обновление адаптера
        photoAdapter.notifyDataSetChanged();
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Удаление прошло успешно
                Toast.makeText(MyAccount.this, "Фотография удалена из хранилища", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Удаление не удалось
                Toast.makeText(MyAccount.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtUsers");
        databaseReference.child(EmailMyAccount).child("userPhotos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(String.class).equals(imageUrl)) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyAccount.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    // Вспомогательный метод для получения расширения файла из URI
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }




    // Не забудьте объявить переменную PICK_IMAGE_REQUEST:
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_ALBUM_IMAGE_REQUEST = 2;
}