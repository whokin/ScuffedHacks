package com.example.scuffeddictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DefinitionDisplay extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition_display);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String word = intent.getStringExtra("WORD");

        recyclerView = findViewById(R.id.definition_recycler_view);
        setAdapter();
    }

    private void setAdapter(){
        ArrayList<String> testString = new ArrayList<String>();
        testString.add("hello!");
        testString.add("helllo1");
        testString.add("helllo2");
        testString.add("helllo3");
        testString.add("helllo4");

        DefinitionRecyclerViewAdapter adapter = new DefinitionRecyclerViewAdapter(testString);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}