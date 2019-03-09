package com.example.john.munchies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    DatabaseHelper DB;
    TextView userID;
    EditText displayName, pass;
    Button goRegister;
    String getuserid, getname, getpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        DB = new DatabaseHelper(this);
        userID = (TextView) findViewById(R.id.editID);
        displayName = (EditText) findViewById(R.id.editDisplayName);
        pass = (EditText) findViewById(R.id.editRegisterPassword);
        goRegister = (Button) findViewById(R.id.btnRegister);

        userID.setText(getIntent().getStringExtra("UserID"));
        //AddAccountDB();

    }

    public void AddAccountDB(){
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getuserid = userID.getText().toString();
                getname = displayName.getText().toString();
                getpass = pass.getText().toString();
                if(getname.equals("") || getpass.equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill in your information", Toast.LENGTH_LONG).show();
                }
                else if(getname.equals("Admin")){
                    Toast.makeText(getApplicationContext(), "Cannot use that name", Toast.LENGTH_LONG).show();
                }
                else{
                    Boolean isAdded = DB.AddUser(getuserid, getname, getpass);
                    if(isAdded == true){
                        displayName.setText("");
                        pass.setText("");
                        Toast.makeText(Registration.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Registration.this, Login.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(Registration.this, "Registration not successful", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void AddAccount(){

    }
}
