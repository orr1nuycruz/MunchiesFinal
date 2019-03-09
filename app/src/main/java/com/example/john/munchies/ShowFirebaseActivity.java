package com.example.john.munchies;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowFirebaseActivity extends AppCompatActivity {

    RecyclerView recycleView;
    TextView sTitle, sContent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_firebase);
        recycleView = (RecyclerView) findViewById(R.id.showData);
        sTitle = (TextView) findViewById(R.id.showTitle);
        sContent = (TextView) findViewById(R.id.showContent);

        firebaseDatabase = FirebaseDatabase.getInstance();
        displayContent();



    }

    private void displayContent(){
        ref = firebaseDatabase.getReference("MunchiesDB").child("Emails").child("ocruz2");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    EmailClass post = dataSnapshot.getValue(EmailClass.class);
                    String getTitle = post.getEmail();
                    String getCom = post.getPassword();
                    sTitle.setText(getTitle);
                    sContent.setText(getCom);
                    String str ="ocruz2@hotmail.com";
                    String kept = str.substring(0, str.indexOf("@"));
                    Toast.makeText(getApplicationContext(), "Heres your data", Toast.LENGTH_LONG).show();
                    if (getTitle.equals("ocruz2@hotmail.com")){
                        Toast.makeText(getApplicationContext(), "thu data showing: " + kept, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Data not found: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
