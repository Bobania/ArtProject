package com.example.artproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TestAccount extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public TestAccount(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // Так как у нас только один элемент
        return 1;
    }

    @Override
    public Object getItem(int position) {
        // Возвращаем null, так как у нас нет реальной модели данных
        return null;
    }

    @Override
    public long getItemId(int position) {
        // Возвращаем ID элемента, в нашем случае позицию
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Проверяем, используется ли уже существующий макет
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.chat_item, parent, false);
        }

        // Находим элементы макета
        TextView userName = convertView.findViewById(R.id.user_name);
        ImageView userAvatar = convertView.findViewById(R.id.user_avatar);

        // Устанавливаем тестовые данные
        userName.setText("Penguin");
        userAvatar.setImageResource(R.drawable.ic_test); // Убедитесь, что у вас есть такой ресурс

        return convertView;
    }


}