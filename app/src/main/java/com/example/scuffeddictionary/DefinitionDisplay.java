package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DefinitionDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition_display);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String word = intent.getStringExtra("SEARCH_WORD");

        TextView definition = findViewById(R.id.result_txt);

        definition.setText(word);
    }
}