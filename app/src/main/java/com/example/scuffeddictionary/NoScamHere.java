package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NoScamHere extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_scam_here);
        getSupportActionBar().hide();
    }
}