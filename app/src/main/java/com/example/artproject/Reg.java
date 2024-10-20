package com.example.artproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Reg extends AppCompatActivity {
    private EditText Email, Password, Name;
    public ProgressDialog loadingBar;
    public String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://planetarium-ab9b2-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Name = (EditText)findViewById(R.id.usernamereg) ;
        Email = (EditText)findViewById(R.id.emailreg);
        Password = (EditText)findViewById(R.id.passwordreg);

        Button backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(this::onClick);

        loadingBar = new ProgressDialog(this);

        Button regBt = (Button)findViewById(R.id.regbutton);

        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String username = Name.getText().toString();
        String useremail = Email.getText().toString();
        String userpswd = Password.getText().toString();

        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Введите имя пользователя!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(useremail)) {
            Toast.makeText(this, "Введите почту!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userpswd)) {
            Toast.makeText(this, "Введите пароль!", Toast.LENGTH_LONG).show();
        }
        else {
            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста, подождите");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidateEmail( username, useremail, userpswd);



        }
    }

    private void ValidateEmail(String username, String useremail, String userpswd) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        String encodedEmail = encodeUserEmail(useremail);

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("ArtUsers").child(encodedEmail).exists())) { // Используем закодированный email

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("User Email", useremail);
                    userDataMap.put("Name", username);
                    userDataMap.put("Password", userpswd);

                    RootRef.child("ArtUsers").child(encodedEmail).updateChildren(userDataMap) // Используем закодированный email
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Reg.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                                        Intent Login = new Intent(Reg.this, Entry.class);
                                        startActivity(Login);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(Reg.this, "Ошибка", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(Reg.this, "Данный пользователь уже существует", Toast.LENGTH_SHORT).show();

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        // Переопределяем метод onBackPressed
        super.onBackPressed();
        // Этот вызов 'super.onBackPressed()' будет обрабатывать возвращение к предыдущей активности в стеке
    }


    public void onClick(View v) {
        if (v.getId() == R.id.newregbutton) {
            Intent intent = new Intent(Reg.this, Entry.class);
            startActivity(intent);
        } else if (v.getId() == R.id.backbutton) {
            onBackPressed();

        }
    }

}

