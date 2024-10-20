package com.example.artproject;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.artproject.R;

public class Navigation extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        TabHost tabHost = getTabHost();



        TabHost.TabSpec tabSpec;



        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_home));
        tabSpec.setContent(new Intent(this, HomePageGallery.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_messages));
        tabSpec.setContent(new Intent(this, Chats.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_subs));
        tabSpec.setContent(new Intent(this, Subscriptions.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.ic_account));
        tabSpec.setContent(new Intent(this, MyAccount.class));
        tabHost.addTab(tabSpec);




    }
}