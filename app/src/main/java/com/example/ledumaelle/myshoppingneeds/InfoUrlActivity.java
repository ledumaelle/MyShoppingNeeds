package com.example.ledumaelle.myshoppingneeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ledumaelle.myshoppingneeds.bo.Article;

public class InfoUrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_url);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Article oneArticle = intent.getParcelableExtra("oneArticle");
        TextView url  = findViewById(R.id.txtUrl);
        url.setText(oneArticle.getUrl());
    }


}