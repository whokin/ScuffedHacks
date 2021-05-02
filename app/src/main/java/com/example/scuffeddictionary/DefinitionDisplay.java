package com.example.scuffeddictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DefinitionDisplay extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> definitionList = new ArrayList<String>();
    DefinitionRecyclerViewAdapter adapter;
    String word;
    Definition definition;
    String definitionStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_definition_display);
//        definition = (TextView) findViewById(R.id.text); // TODO: change the ID

        getSupportActionBar().hide();

        Intent intent = getIntent();
        word = intent.getStringExtra("WORD");

        fillDefinitionComponents();

        definition = new Definition(word);
        definition.execute();

        setAdapter();

//        for(word :definitionList){
//            httpGET(word);
//        }
//        System.out.println("The synonym of duck is " + Arrays.toString(getSynonym("duck")));

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

//        String ogWord = definitionList.get(1);
//        System.out.println("The scuffed up definition is " + scuffify(ogWord));
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

    private String[] getSynonym(String wordToSearch){
        final String[] synonym = {""};

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://words.bighugelabs.com/api/2/79ff02e5d9fe9541631c82d4a6d507e1/" + wordToSearch;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("API", "Response is: "+ response);

                        // Parse the data
                        int newLineIndex = response.indexOf("\n");
                        String first_line = response.substring(0, newLineIndex);

                        String syn = first_line.split("\n")[0].split("\\|")[2];
                        Log.d("API", syn);

                        System.out.println("The first line is " + first_line + "\n");
                        System.out.println("The syn is " + syn + "\n");

                        synonym[0] = syn;

                        // store it somehow


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
                Toast.makeText(DefinitionDisplay.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return synonym;
    }

    private String scuffify(String definition) {
        String[] allWords = definition.toLowerCase().split(" ");

        StringBuilder builder = new StringBuilder();
        for(String syn : allWords) {
            // Find synonym
            if(!getSynonym(syn).toString().isEmpty()){
                syn = getSynonym(syn).toString();
            }
            builder.append(syn);
            builder.append(' ');
        }

        return builder.toString().trim();
    }
//    parse using | sep
//    split on new line and remove until the actual synonym
//    swap words



}