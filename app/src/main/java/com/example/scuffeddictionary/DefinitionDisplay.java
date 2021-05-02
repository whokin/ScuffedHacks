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
    ArrayList<String> scuffedDefinitionList = new ArrayList<String>();
    DefinitionRecyclerViewAdapter adapter;
    String word;
    Definition definition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_definition_display);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        word = intent.getStringExtra("WORD");

        fillDefinitionComponents();

        definition = new Definition(word);
        definition.execute();

        setAdapter();
    }

    private void fillDefinitionComponents() {

        TextView wordTextView = findViewById(R.id.txt_word_defndisplay);
        wordTextView.setText(word.toLowerCase());

    }

    private class Definition extends AsyncTask<Void, Void, Void> {

        String wordType;
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

                //get word type
                Elements wordTypeElement = doc.select("a.important-blue-link");
                wordType = extractWordTypeText(wordTypeElement.toString());
                System.out.println("WORD TYPE IN DOINBACKGROUND: " + extractWordTypeText(wordTypeElement.toString()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView wordTypeTextView = findViewById(R.id.txt_wordtype_defndisplay);
                        wordTypeTextView.setText(wordType);
                    }
                });

                //get definition
                Elements elements = doc.select("span.dtText");

                for (int i = 0; i < elements.size(); i++){
                    //use regex to extract definition
                    String definition = extractDefinitionText(elements.get(i).toString());

                    //add to recyclerview
                    if (!definition.equals("no matches")){
                        definitionList.add(definition);
                    }
                }

                for (int i = 0; i < definitionList.size(); i++){

                    String words[] = definitionList.get(i).split(" ");
                    String scuffedDefinition = "";

                    //for each word per definition
                    for (int j = 0; j < words.length; j++){

                        String word = words[j];
                        String thesaurusUrl = "https://www.merriam-webster.com/thesaurus/" + word + "/";
                        Document document = Jsoup.connect(thesaurusUrl).get();

                        //get definition
                        Elements synElements = document.select("ul.mw-list");

                        String synonym = extractSynonym(synElements.toString());

                        //if (synonym != "no matches"){
                            scuffedDefinition = scuffedDefinition + synonym + " ";
                        //}
                     }

                    scuffedDefinitionList.add(scuffedDefinition);
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
        adapter = new DefinitionRecyclerViewAdapter(scuffedDefinitionList);
        recyclerView.setAdapter(adapter);
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

    public String extractSynonym(String string){
        Pattern definitionPattern = Pattern.compile("thesaurus(.*)>(.*?)</a>");
        Matcher matcher = definitionPattern.matcher(string);
        if (matcher.find())
        {
            return matcher.group(2);
        }
        return "no matches";
    }

}