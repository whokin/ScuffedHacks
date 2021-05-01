package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

public class DefinitionDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition_display);

        Intent intent = getIntent();
        String word = intent.getStringExtra("DEFINITION");

        TextView definition = findViewById(R.id.result_txt);

        definition.setText(word);

        Content content = new Content();

        content.execute();
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://www.merriam-webster.com/dictionary/poultry?src=search-dict-hed";
                //Connect to the website
                Document document = Jsoup.connect(url).get();

                //Get the dictionary definitions from the website
                Elements data = document.select("dtText");

                // Get the number of definitions
                int size = data.size();

                for(int i = 0; i < size; i++){
                    String definition = data.select("dtText")
                            .select("mw_t_bc")
                            .eq(i)
                            .text();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}