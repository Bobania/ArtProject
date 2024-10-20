package com.example.artproject;

import android.app.Application;

public class GlobalVars extends Application {
    private String EmailMyAccount;
    private String NameMyAccount;

    public String getNameMyAccount() {
        return NameMyAccount;
    }
    public String getEmailMyAccount() {
        return EmailMyAccount;
    }
    public void setNameMyAccount(String name) {
        NameMyAccount = name;
    }
    public void setEmailMyAccount(String email) {
        EmailMyAccount = email;
    }
}