package com.example.artproject;

import java.util.Date;

public class SendMessages {
    public String userName;
    public String textMessage;
    private long messageTime;

    // Публичный конструктор без аргументов
    public SendMessages() {
        // Firebase требует наличие пустого конструктора
    }

    // Конструктор с параметрами
    public SendMessages(String userName, String textMessage) {
        this.userName = userName;
        this.textMessage = textMessage;
        // Инициализация messageTime текущим временем
        this.messageTime = new Date().getTime();
    }

    // Геттеры и сеттеры
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    // Исправлена опечатка в названии метода
    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}