package com.example.artproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Chats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        ListView listView = findViewById(R.id.list_of_messages);
        TestAccount adapter = new TestAccount(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Создаем интент для перехода в активность Messages
                Intent intent = new Intent(Chats.this, Messages.class);

                // Можно добавить дополнительные данные, если нужно
                // intent.putExtra("key", "value");

                // Запускаем активность
                startActivity(intent);
            }
        });
    }
}