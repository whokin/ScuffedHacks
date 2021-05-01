package com.example.scuffeddictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DefinitionRecyclerViewAdapter extends RecyclerView.Adapter<DefinitionRecyclerViewAdapter.MyViewHolder>{

    ArrayList<String> definitions;

    public DefinitionRecyclerViewAdapter(ArrayList<String> definitions){
        this.definitions = definitions;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView definitionTxt;


        public MyViewHolder(final View view){
            super(view);
            definitionTxt = view.findViewById(R.id.definition_txt);
        }
    }

    @NonNull
    @Override
    public DefinitionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View definitionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_item, parent, false);
        return new MyViewHolder(definitionView);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String definition = definitions.get(position);
        holder.definitionTxt.setText(definition);
    }

    @Override
    public int getItemCount() {
        return definitions.size();
    }


}

