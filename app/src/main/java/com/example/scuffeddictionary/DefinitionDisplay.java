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
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;


public class DefinitionDisplay extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> definitionList = new ArrayList<String>();
    DefinitionRecyclerViewAdapter adapter;
    String word;
    Definition definition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_definition_display);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        word = intent.getStringExtra("WORD");

        fillDefinitionComponents();

        if(!word.equals("scuffed")){
            definition = new Definition(word);
            definition.execute();
        }else{
            TextView wordTypeTextView = findViewById(R.id.txt_wordtype_defndisplay);
            wordTypeTextView.setText("adjective");

            TextView speechTextView = findViewById(R.id.txt_speech_defndisplay);
            speechTextView.setText("/ skəffdtzdfadfe /");

            definitionList.add("the theme of the best hackathon to ever have existed");
            definitionList.add("this application");

        }

        setAdapter();
    }

    private void fillDefinitionComponents() {

        TextView wordTextView = findViewById(R.id.txt_word_defndisplay);
        wordTextView.setText(word.toLowerCase());

    }

    private class Definition extends AsyncTask<Void, Void, Void> {

        String wordType;
        String speechWord;
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
                Document doc = Jsoup.connect(url).timeout(1000).get();

                //get word type
                Elements wordTypeElement = doc.select("a.important-blue-link");
                wordType = extractWordTypeText(wordTypeElement.toString());
                Elements speechElement = doc.select("span.pr");
                speechWord = extractTags(speechElement.toString());


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView wordTypeTextView = findViewById(R.id.txt_wordtype_defndisplay);
                        wordTypeTextView.setText(wordType);

                        TextView speechTextView = findViewById(R.id.txt_speech_defndisplay);
                        speechTextView.setText("/ " + speechWord + "   /");
                    }
                });

                //get definition
                Elements elements = doc.select("span.dtText");
                int elementsSize = elements.size();

                if(elementsSize > 3){
                    elementsSize = 3;
                }

                for (int i = 0; i < elementsSize; i++){
                    //use regex to extract definition
                    String definition = extractDefinitionText(elements.get(i).toString());

                    //add to recyclerview
                    if (!definition.equals("no matches")){
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

    private String extractTags(String string) {
        System.out.println(string);
        Pattern pattern = Pattern.compile("<span class=\"pr\">(.+?)</span>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }

    public String extractDefinitionText(String string){
        Pattern definitionPattern = Pattern.compile("</strong>(.*?)</span>");
        Matcher matcher = definitionPattern.matcher(string);
        if (matcher.find())
        {
            if (matcher.group(1).contains("<") || matcher.group(1).contains(">") ){
                return "no matches";
            }
            return(matcher.group(1));
        }

        return "no matches";
    }

    public String extractWordTypeText(String string){
        Pattern definitionPattern = Pattern.compile(">(.*?)</a>");
        Matcher matcher = definitionPattern.matcher(string);
        if (matcher.find())
        {
            if (matcher.group(1).contains("<") || matcher.group(1).contains(">") ){
                return "no matches";
            }
            return(matcher.group(1));
        }

        return "no matches";
    }

}