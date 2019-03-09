package com.example.john.munchies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView txtVTitle, txtVContent;

    public MyRecyclerViewHolder(@NonNull View itemView){
        super (itemView);
        txtVTitle = (TextView)itemView.findViewById(R.id.showTitle);
        txtVContent = (TextView)itemView.findViewById(R.id.showContent);


    }
}
