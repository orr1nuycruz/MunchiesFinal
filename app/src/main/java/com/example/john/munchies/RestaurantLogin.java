package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurantLogin extends AppCompatActivity {
//
    EditText txtUser, txtPass;
    String getUser, getPass;
    Button btnLogin;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_login);
        txtUser = (EditText) findViewById(R.id.editRestUser);
        txtPass = (EditText) findViewById(R.id.editRestPass);
        btnLogin = (Button) findViewById(R.id.restLgn);
        firebaseDatabase = FirebaseDatabase.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUser = txtUser.getText().toString();
                getPass = txtPass.getText().toString();

                if (getUser.equals("") || getPass.equals("")  ){
                    Toast.makeText(getApplicationContext(), "Empty Strings", Toast.LENGTH_SHORT).show();
                }
                else{
                    ref = firebaseDatabase.getReference("MunchiesDB").child("RestaurantsUsers").child(getUser);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RestaurantUserClass getUsers = dataSnapshot.getValue(RestaurantUserClass.class);
                            String name = getUsers.getRestaurantName();
                            String user = getUsers.getUsername();
                            String pass = getUsers.getPassword();
                            if(getUser.equals(user) && getPass.equals(pass)){
                                Toast.makeText(getApplicationContext(), "Restaurant found", Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("User", getUser);
                                editor.putString("RestName", name);
                                editor.apply();
                                Intent i = new Intent(RestaurantLogin.this, RestaurantMenuCRUD.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Restaurant user not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Data not found: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


    }

    public void goToReg(View v){
        Intent i = new Intent(this, RestaurantRegistration.class);
        startActivity(i);
    }
}
