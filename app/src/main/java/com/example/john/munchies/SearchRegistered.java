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

    DatabaseHelper DB;
    EditText userID, password;
    Button goRegister;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String getCutUser, getUser, getPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_registered);

        //initialize
        firebaseAuth = FirebaseAuth.getInstance();


        //DB = new DatabaseHelper(this);

        userID = (EditText) findViewById(R.id.LoginUserID);
        password = (EditText) findViewById(R.id.LoginPassword);
        goRegister = (Button) findViewById(R.id.nextPage);

        progressDialog = new ProgressDialog(this);



        //Our views
        goRegister.setOnClickListener(this);

        //goRegDB();
    }



    public void goReg(){

        String user = userID.getText().toString();
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) ){
            Toast.makeText(this, "Empty String Forbidden", Toast.LENGTH_SHORT).show();
            return;

        }

        if (!user.endsWith("@my.centennialcollege.ca")) {
            Toast.makeText(this, "Only Centennial Email Allowed", Toast.LENGTH_SHORT).show();
            return;
        }


        else{
            //therefore it must be created
            progressDialog.setMessage("Processing Registration");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
    }

    @Override
    public void onClick(View view){
        //I need to compare it to something
        if (view == goRegister){
                goReg();
        }
    }


    public void goRegDB(){
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser = userID.getText().toString();
                getPass = password.getText().toString();
                if(getUser.equals("")|| getPass.equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill in your information", Toast.LENGTH_LONG).show();
                }
                else{
                    Cursor cursor = DB.getRegisteredSchoolUser(getUser, getPass);
                    Cursor cursor2 = DB.checkUser(getUser);
                    if (cursor.getCount() == 0 ) {
                        Toast.makeText(getApplicationContext(), "Invalid Credential", Toast.LENGTH_LONG).show();
                        userID.setText("");
                        password.setText("");
                        return;
                    }
                    else if(cursor2.getCount() > 0){
                        Toast.makeText(getApplicationContext(), "User for this app exists. Cannot proceed", Toast.LENGTH_LONG).show();
                        userID.setText("");
                        password.setText("");
                        return;
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Registered User Exists.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SearchRegistered.this, Registration.class);
                        i.putExtra("UserID", getUser);
                        userID.setText("");
                        password.setText("");
                        startActivity(i);
                    }
                }
            }
        });

    }


}
