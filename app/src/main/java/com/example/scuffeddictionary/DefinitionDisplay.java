package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;


public class DefinitionDisplay extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> definitionList = new ArrayList<String>();
    DefinitionRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_definition_display);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String word = intent.getStringExtra("WORD");

        setAdapter();

        Definition definition = new Definition(word);
        definition.execute();
    }

    private class Definition extends AsyncTask<Void, Void, Void> {

        String word;
        public Definition(String word){
            this.word = word;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url = "https://www.merriam-webster.com/dictionary/" + word + "/";

            try {
                Document doc = Jsoup.connect(url).timeout(6000).get();
                Elements elements = doc.select("span.dtText");

                for (int i = 0; i < elements.size(); i++){
                    String definition = extractElementText(elements.get(i).toString());

                    if (!definition.equals("no matches found")){
                        definitionList.add(definition);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void setAdapter(){
        recyclerView = findViewById(R.id.definition_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DefinitionRecyclerViewAdapter(definitionList);
        recyclerView.setAdapter(adapter);
    }

    public String extractElementText(String string){
        Pattern definitionPattern = Pattern.compile("</strong>(.*?)</span>");
        Matcher matcher = definitionPattern.matcher(string);
        if (matcher.find())
        {
            if (matcher.group(1).contains("<") || matcher.group(1).contains(">") ){
                return "no matches found";
            }
            return(matcher.group(1));
        }

        return "no matches found";
    }

}