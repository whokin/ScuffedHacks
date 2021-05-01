package com.example.scuffeddictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WordParser {
    private String definition;
    private  String scuffedDefinition;
    private ArrayList stopwords;

    public WordParser(String definition) throws IOException {
        // Load stopwords
        loadStopwords();

        // Remove stopwords
        String words = removeStopwords();

        // Replace with synonyms
        scuffify(words);

        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    public String getScuffedDefinition() {
        return scuffedDefinition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setScuffedDefinition(String scuffedDefinition) {
        this.scuffedDefinition = scuffedDefinition;
    }

    private void loadStopwords() throws IOException {
//        stopwords = Files.readAllLines(Paths.get("english_stopwords.txt"));

        // Manually making a list of stop words
        List<String> stopwords=new ArrayList<String>();
        //Adding elements in the List
        stopwords.add("the");
        stopwords.add("over");

        this.stopwords = (ArrayList) stopwords;
    }

    private String removeStopwords() {
        String[] allWords = definition.toLowerCase().split(" ");

        StringBuilder builder = new StringBuilder();
        for(String word : allWords) {
            if(!stopwords.contains(word)) {
                builder.append(word);
                builder.append(' ');
            }
        }

        String result = builder.toString().trim();
        return result;
    }

    private String findSynonym(String word) {
        String synonym = "";

        // scrape a synonym from the web
        // or
        // get using API

        return synonym;
    }
    
    private void scuffify(String words) {
        String[] allWords = words.toLowerCase().split(" ");

        StringBuilder builder = new StringBuilder();
        for(String word : allWords) {
            // Find synonym
            if(!findSynonym(word).isEmpty()){
                word = findSynonym(word);
            }
            builder.append(word);
            builder.append(' ');
        }

        scuffedDefinition = builder.toString().trim();
    }
}
