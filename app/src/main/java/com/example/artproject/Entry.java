package com.example.artproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Entry extends AppCompatActivity implements View.OnClickListener{
    private EditText Email, Password;
    public String EmailMyAccount;
    public ProgressDialog loadingBar;
    private Button loginBt;
    private String parentDbName = "ArtUsers";
    public String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://planetarium-ab9b2-default-rtdb.firebaseio.com/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);

        Button newregbutton = findViewById(R.id.newregbutton);
        newregbutton.setOnClickListener(this);
        FirebaseApp.initializeApp(this);


        loginBt = (Button)findViewById(R.id.vhodbutton);
        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        loadingBar = new ProgressDialog(this);

        Button regBt = (Button)findViewById(R.id.newregbutton);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUsr();
            }
        });
    }



    private void LoginUsr() {
        String useremail = Email.getText().toString();
        String userpswd = Password.getText().toString();

        if(TextUtils.isEmpty(useremail)) {
            Toast.makeText(this, "Введите почту!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userpswd)) {
            Toast.makeText(this, "Введите пароль!", Toast.LENGTH_LONG).show();
        }
        else {
            loadingBar.setTitle("Вход в аккаунт");
            loadingBar.setMessage("Пожалуйста, подождите");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidateUser(useremail, userpswd);
        }
    }

    private void ValidateUser(String useremail, String userpswd) {
        String encodedEmail = encodeUserEmail(useremail);
        //final DatabaseReference RootRef;
        //RootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ArtUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.hasChild(encodedEmail)) {
                    final String getPassword = snapshot.child(useremail).child("Password").getValue(String.class);
                    final String getName = snapshot.child(useremail).child("Name").getValue(String.class);

                    if (getPassword.equals(userpswd)) {
                        ((GlobalVars) getApplicationContext()).setEmailMyAccount(useremail);
                        ((GlobalVars) getApplicationContext()).setNameMyAccount(getName);

                        loadingBar.dismiss();
                        Toast.makeText(Entry.this, "Вы зашли как " + useremail, Toast.LENGTH_SHORT).show();
                        Intent Common = new Intent(Entry.this, com.example.artproject.Navigation.class);
                        startActivity(Common);

                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(Entry.this, "Неверный пароль!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(Entry.this, "Данного имени пользователя не существует!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newregbutton) {
            Intent intent = new Intent(Entry.this, Reg.class);
            startActivity(intent);
        }
    }
}