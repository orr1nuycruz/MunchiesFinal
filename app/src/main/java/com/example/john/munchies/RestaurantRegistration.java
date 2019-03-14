package com.example.john.munchies;

import android.content.Intent;
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

public class RestaurantRegistration extends AppCompatActivity {

    EditText restName, restID, restUser, restFPass, restPass;
    Button reg;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref, ref2;
    String getName, getID, getUser, getFPass, getPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_registration);
        restName = (EditText) findViewById(R.id.editRestaurantName);
        restID = (EditText) findViewById(R.id.editID);
        restUser = (EditText) findViewById(R.id.editRestaurantDisplayName);
        restFPass = (EditText) findViewById(R.id.editRestaurantFirstPassword);
        restPass = (EditText)  findViewById(R.id.editRestaurantRegisterPassword);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("MunchiesDB");

        Button goREG = (Button) findViewById(R.id.btnRegister);

        goREG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getName = restName.getText().toString();
                getID = restID.getText().toString();
                getUser = restUser.getText().toString();
                getFPass = restFPass.getText().toString();
                getPass = restPass.getText().toString();

                if (getName.equals("") || getID.equals("")  || getUser.equals("") || getFPass.equals("") || getPass.equals("")  ){
                    Toast.makeText(getApplicationContext(), "Empty Strings", Toast.LENGTH_SHORT).show();
                }
                else if(!getPass.equals(getFPass)){
                    Toast.makeText(getApplicationContext(), "Confirm the right password", Toast.LENGTH_SHORT).show();
                }
                else{
                    ref = firebaseDatabase.getReference("MunchiesDB").child("Restaurants").child(getName);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RestaurantClass rest = dataSnapshot.getValue(RestaurantClass.class);
                            String restName = rest.getRestaurantName();
                            String restID = rest.getRestaurantID();
                            if(getName.equals(restName) && getID.equals(restID)){
                                Toast.makeText(getApplicationContext(), "Restaurant found", Toast.LENGTH_SHORT).show();
                                ref2 = firebaseDatabase.getReference("MunchiesDB").child("RestaurantsUsers");
                                RestaurantUserClass resclass = new RestaurantUserClass(getName, getUser, getPass);
                                ref2.child(getUser).setValue(resclass);
                                Toast.makeText(getApplicationContext(), "Restaurant user added", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RestaurantRegistration.this, RestaurantLogin.class);
                                startActivity(i);

                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Restaurant not found", Toast.LENGTH_SHORT).show();
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
}
