package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class WordSearch extends AppCompatActivity {

    EditText searchWord;
    Button search;
    Button wordOfTheDay;
    String word;
    String definition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_word_search);
        getSupportActionBar().hide();

        searchWord = findViewById(R.id.search_txt);

        search = findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = searchWord.getText().toString();
                if(word.length() > 0){
                    Intent intent = new Intent(WordSearch.this, DefinitionDisplay.class);
                    intent.putExtra("WORD", word);
                    startActivity(intent);
                }else{
                    Toast.makeText(WordSearch.this, "Please enter word to Scuffify", Toast.LENGTH_SHORT).show();
                }

            }
        });

        wordOfTheDay = findViewById(R.id.see_definition_btn);
        wordOfTheDay.setVisibility(View.VISIBLE);
        wordOfTheDay.setBackgroundColor(Color.TRANSPARENT);
        wordOfTheDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordSearch.this, DefinitionDisplay.class);
                intent.putExtra("WORD", "scuffed");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchWord.setText("");
    }
}