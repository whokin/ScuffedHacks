package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WordSearch extends AppCompatActivity {

    EditText searchWord;
    Button search;
    String word;
    String definition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_search);
        getSupportActionBar().hide();

        searchWord = findViewById(R.id.search_txt);
        search = findViewById(R.id.search_button);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = searchWord.getText().toString();
                definition = getDefinition(word);
                Intent intent = new Intent(WordSearch.this, DefinitionDisplay.class);
                intent.putExtra("DEFINITION", definition);
                startActivity(intent);
            }
        });
    }

    String getDefinition(String word){
        return word;
    }
}