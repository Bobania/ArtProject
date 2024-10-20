package com.example.artproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.text.format.DateFormat;
import java.util.Date;

public class Messages extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://planetarium-ab9b2-default-rtdb.firebaseio.com/");

    private FloatingActionButton sendBtn;
    private FirebaseListAdapter<SendMessages> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        String NewEmailMyAccount = ((GlobalVars) getApplicationContext()).getEmailMyAccount();
        String NewNameMyAccount = ((GlobalVars) getApplicationContext()).getNameMyAccount();
        displayAllMessages();
        sendBtn = findViewById(R.id.btnSend);

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText textField = findViewById(R.id.message_field);
                String messageText = textField.getText().toString();
                if (!messageText.isEmpty()) {
                    // Получаем имя пользователя из текущего пользователя в системе
                    // Предполагаем, что имя пользователя сохранено в SharedPreferences или в статической переменной
                    String username = getCurrentUserName(); // Замените этот метод на вашу логику получения имени пользователя

                    // Создаем новый объект сообщения
                    SendMessages newMessage = new SendMessages(username, messageText);

                    // Отправляем сообщение в базу данных
                    FirebaseDatabase.getInstance().getReference("messages").push().setValue(newMessage);

                    // Очищаем поле ввода
                    textField.setText("");
                }
            }

            private String getCurrentUserName() {

                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                return prefs.getString("username", NewNameMyAccount); // "Аноним" - значение по умолчанию

            }
        });
    }

    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("messages");

        FirebaseListOptions<SendMessages> options = new FirebaseListOptions.Builder<SendMessages>()
                .setLifecycleOwner(this)
                .setQuery(databaseReference, SendMessages.class)
                .setLayout(R.layout.list_item)
                .build();

        adapter = new FirebaseListAdapter<SendMessages>(options) {
            @Override
            protected void populateView(View v, SendMessages model, int position) {

                Log.d("populateView", "Заполнение представления для позиции: " + position);
                TextView mess_user = v.findViewById(R.id.message_user);
                TextView mess_time = v.findViewById(R.id.message_time);
                TextView mess_text = v.findViewById(R.id.message_text);


                mess_user.setText(model.getUserName());
                mess_text.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("HH:mm", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        // Переопределяем метод onBackPressed
        super.onBackPressed();
        // Этот вызов 'super.onBackPressed()' будет обрабатывать возвращение к предыдущей активности в стеке
    }
    public void onClick(View view) {
        // Проверяем, что нажатие пришло от кнопки с id backbuttonmess
        if (view.getId() == R.id.backbuttonmess) {
            // Имитируем нажатие системной кнопки "назад"
            onBackPressed();
        }
    }
}