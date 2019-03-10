package com.example.john.munchies;
//Tested for ending - therefore only accepting centennial emails
//we added a progress dialog - Internet may take some time
//I guess we can go to the login page and test if it exists

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchRegistered extends AppCompatActivity implements View.OnClickListener {

    EditText userID, password, emailPassword;
    Button goRegister;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    String user, cutUser, ePass, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_registered);

        //initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        userID = (EditText) findViewById(R.id.LoginUserID);
        emailPassword = (EditText)  findViewById(R.id.emailPass);
        password = (EditText) findViewById(R.id.LoginPassword);
        goRegister = (Button) findViewById(R.id.nextPage);

        progressDialog = new ProgressDialog(this);

        //Our views
        goRegister.setOnClickListener(this);

        //goRegDB();
    }



    public void goReg(){

        user = userID.getText().toString();
        cutUser = user.substring(0, user.indexOf("@"));
        ePass = emailPassword.getText().toString();
        pass = password.getText().toString();

        if (user.equals("") || pass.equals("")  || ePass.equals("")  ){
            Toast.makeText(this, "Empty String Forbidden", Toast.LENGTH_SHORT).show();
        }

        if (!user.endsWith("@my.centennialcollege.ca")) {
            Toast.makeText(this, "Only Centennial Email Allowed", Toast.LENGTH_SHORT).show();
        }

        else{
            ref = firebaseDatabase.getReference("MunchiesDB").child("Emails").child(cutUser);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    EmailClass check = dataSnapshot.getValue(EmailClass.class);
                    String getEmail = check.getEmail();
                    String getP = check.getPassword();
                    if(user.equals(getEmail) && ePass.equals(getP)){
                        Toast.makeText(getApplicationContext(), "User found", Toast.LENGTH_LONG).show();
                        //therefore it must be created
                        progressDialog.setMessage("Processing Registration");
                        progressDialog.show();
                        firebaseAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(SearchRegistered.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SearchRegistered.this, "REGISTERED", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();

                                    Intent i = new Intent(SearchRegistered.this, Login.class);
                                    startActivity(i);

                                }
                                else {
                                    progressDialog.cancel();
                                    Toast.makeText(SearchRegistered.this, "NOT REGISTERED - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.cancel();

                            }
                        });


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "user not found", Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Data not found: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void onClick(View view){
        //I need to compare it to something
        if (view == goRegister){
                goReg();
        }
    }



}
