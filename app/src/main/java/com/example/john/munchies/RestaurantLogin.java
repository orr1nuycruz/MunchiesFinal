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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurantLogin extends AppCompatActivity {

    EditText txtUser, txtPass;
    String getUser, getPass;

    TextView loginMessage;
    Button btnLogin, btnGoReg, btnGoAddItems, btnLogout, btnComplaint, btnMakeComp, btnGoViewItems;
    LinearLayout viewUser, viewPass;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_login);
        txtUser = (EditText) findViewById(R.id.editRestUser);
        txtPass = (EditText) findViewById(R.id.editRestPass);
        viewUser = (LinearLayout) findViewById(R.id.userView);
        viewPass = (LinearLayout) findViewById(R.id.passView);

        loginMessage = (TextView) findViewById(R.id.txtloginMessage);

        btnGoReg = (Button) findViewById(R.id.goRegister);
        btnLogin = (Button) findViewById(R.id.restLgn);
        btnGoAddItems = (Button) findViewById(R.id.goToAddItems);
        btnLogout = (Button) findViewById(R.id.logout);
        btnGoViewItems = (Button) findViewById(R.id.goToViewItems);
        btnComplaint = (Button) findViewById(R.id.goToComplaint);
        btnMakeComp = (Button) findViewById(R.id.goComplain);

        firebaseDatabase = FirebaseDatabase.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestLogin();

            }
        });

        btnGoAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddItems();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        btnGoViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewItems();
            }
        });


        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowComplaintList();
            }
        });

        btnMakeComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMakeComp();
            }
        });

        configView();

    }


    public void goToReg(View v){
        Intent i = new Intent(this, RestaurantRegistration.class);
        startActivity(i);
    }

    public void goToAddItems(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent i = new Intent(this, RestaurantMenuCRUD.class);
        startActivity(i);
    }

    public void configView(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restuser = preferences.getString("RestName", "");
        if(preferences.contains("User") && preferences.contains("RestName")){
            viewUser.setVisibility(View.INVISIBLE);
            viewPass.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            btnGoReg.setVisibility(View.INVISIBLE);
            btnGoAddItems.setVisibility(View.VISIBLE);
            btnGoViewItems.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            btnComplaint.setVisibility(View.VISIBLE);
            btnMakeComp.setVisibility(View.VISIBLE);
            loginMessage.setText(restuser);
        }
        else{
            viewUser.setVisibility(View.VISIBLE);
            viewPass.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnGoReg.setVisibility(View.VISIBLE);
            btnGoViewItems.setVisibility(View.INVISIBLE);

            btnGoAddItems.setVisibility(View.INVISIBLE);
            btnLogout.setVisibility(View.INVISIBLE);
            btnComplaint.setVisibility(View.INVISIBLE);
            btnMakeComp.setVisibility(View.VISIBLE);
            loginMessage.setText("Restaurant Login");
        }

    }

    public void goToViewItems(){
        Intent i = new Intent(RestaurantLogin.this, RestaurantViewOrders.class);
        startActivity(i);

    }
    public void Logout(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().remove("User").commit();
        preferences.edit().remove("RestName").commit();
        Intent i = new Intent(RestaurantLogin.this, RestaurantLogin.class);
        startActivity(i);

    }

    public void ShowComplaintList(){
        Intent i = new Intent(RestaurantLogin.this, ComplaintList.class);
        startActivity(i);

    }

    public void gotoMakeComp(){
        Intent i = new Intent(this, RestauantComplaint.class);
        startActivity(i);
    }



    public void RestLogin(){
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
                        //Intent i = new Intent(RestaurantLogin.this, RestaurantMenuCRUD.class);
                        Intent i = new Intent(RestaurantLogin.this, RestaurantLogin.class);
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
}
