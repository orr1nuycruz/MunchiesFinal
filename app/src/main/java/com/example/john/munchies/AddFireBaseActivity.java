package com.example.john.munchies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFireBaseActivity extends AppCompatActivity {

    EditText titleTxt, contentTxt;
    Button submitBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref, ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fire_base);
        titleTxt = (EditText) findViewById(R.id.title);
        contentTxt = (EditText)findViewById(R.id.content);
        submitBtn = (Button) findViewById(R.id.add);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("MunchiesDB");
        ref2 = firebaseDatabase.getReference("MunchiesDB2");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PostComment();
                PostUser();
            }
        });
    }

    public void PostComment(){
        String title = titleTxt.getText().toString();
        String content = contentTxt.getText().toString();
        Post post = new Post(title, content);
        ref.child("Comments_Test").child(title).setValue(post);

    }

    public void PostUser(){
        String title = titleTxt.getText().toString();
        String content = contentTxt.getText().toString();
        String kept = title.substring(0, title.indexOf("@"));
        EmailClass post = new EmailClass(title, content);
        ref.child("Emails").child(kept).setValue(post);
    }



}
