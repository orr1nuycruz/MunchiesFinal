package com.example.john.munchies;
//Harvey Cabrias
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerCreditCardList extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    ListView customerCreditCardList;
    ArrayList<String> customerCreditCardArrayList;
    ArrayAdapter<String> customerCreditCardAdapter;

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_credit_card_list);

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB");

        customerCreditCardArrayList = new ArrayList<String>();

        customerCreditCardList = (ListView)findViewById(R.id.customerCreditCardList);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = sharedPref.getString("customerEmail", "");
        displayCreditCardList();
    }


    //Display the CreditCard from the database
    public void displayCreditCardList(){
        myRef = myFB.getReference("MunchiesDB").child("CustomerCreditCards").child("harvey");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showData(DataSnapshot dataSnapshot){
        customerCreditCardArrayList.clear();
        for(DataSnapshot cards: dataSnapshot.getChildren()){
            CreditCardClass creditCard = cards.getValue(CreditCardClass.class);
            customerCreditCardArrayList.add(creditCard.getCreditCardNumber());
        }

        customerCreditCardAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, customerCreditCardArrayList);
        customerCreditCardList.setAdapter(customerCreditCardAdapter);

    }
}
