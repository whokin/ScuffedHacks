package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WordSearch extends AppCompatActivity {

    EditText searchWord;
    Button search;
    String word;
    String definition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_search);

        searchWord = findViewById(R.id.search_txt);
        search = findViewById(R.id.search_button);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = searchWord.getText().toString();
                Definition definition = new Definition(word);
                definition.execute();
            }
        });
    }



    private class Definition extends AsyncTask<Void, Void, Void> {

        String word;

        public Definition(String word){
            this.word = word;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url = "https://www.merriam-webster.com/dictionary/" + word + "/";

            try {
                Document doc = Jsoup.connect(url).timeout(6000).get();
                Elements elements = doc.getElementsByClass("dtText");
                String definition = elements.get(0).toString();

                Intent intent = new Intent(WordSearch.this, DefinitionDisplay.class);
                intent.putExtra("DEFINITION", definition);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}