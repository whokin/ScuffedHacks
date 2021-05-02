package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_definition_display);
//        definition = (TextView) findViewById(R.id.text); // TODO: change the ID


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

            scuffedDefinitionList.add("thy humble choice of theme yond leadeth to the production of this quite quaint application");
            scuffedDefinitionList.add("the theme of the best hackathon to ever have existed");
            scuffedDefinitionList.add("this application");

        }

        setAdapter();

    }

    private void fillDefinitionComponents() {

        TextView wordTextView = findViewById(R.id.txt_word_defndisplay);
        wordTextView.setText(word.toLowerCase());

        ImageView ad = findViewById(R.id.img_ad_defndisplay);
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert ad class here !!!

            //    Intent intent = new Intent(DefinitionDisplay.this, .class);
                //    startActivity(intent);
            }
        });

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

                for (int i = 0; i < elements.size(); i++){
                    //use regex to extract definition
                    String definition = extractDefinitionText(elements.get(i).toString());

                    //add to recyclerview
                    if (!definition.equals("no matches")){
                        definitionList.add(definition);
                    }
                }

                int definitionSize = definitionList.size();
                if(definitionSize > 5){
                    definitionSize = 5;
                }

                for (int i = 0; i < definitionSize; i++){

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

                        if (synonym != "no matches"){
                            scuffedDefinition = scuffedDefinition + synonym + " ";
                        }
                        else{
                            scuffedDefinition = scuffedDefinition + word + " ";
                        }
                     }
                    int num = i + 1;
                    scuffedDefinitionList.add("("+num+") " + scuffedDefinition);
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