package com.example.john.munchies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerPayment extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    TextView itemList;
    TextView totalCost;

    Spinner creditCard;

    String userEmail;
    String customerEmail;
    String orderNumber;
    String displayOrderList;
    String displayPrice;


    ArrayList<String> customerCreditCardArrayList;
    ArrayAdapter<String> customerCreditCardAdapter;

    Button btnPayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        myFB = FirebaseDatabase.getInstance();

        customerCreditCardArrayList = new ArrayList<String>();

        itemList = (TextView)findViewById(R.id.displayListOfItems);
        creditCard = (Spinner)findViewById(R.id.paymentCreditCardList);
        totalCost = (TextView)findViewById(R.id.displayTotalCost);

        btnPayment = (Button)findViewById(R.id.btnPaymentProceed);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = sharedPref.getString("customerEmail", "");
        orderNumber = sharedPref.getString("orderNum", "");

        String getCustomerCreditCardEmail = userEmail.toString();
        customerEmail = getCustomerCreditCardEmail.substring(0, getCustomerCreditCardEmail.indexOf("@"));

        displayCreditCardList();
        displayListItem();
        ProceedPayment();
    }

    //Display List Item
    public void displayListItem() {
        String getOrderNumber = orderNumber.toString();
        myRef = myFB.getReference("MunchiesDB").child("OrderHistory").child(customerEmail).child(getOrderNumber);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    OrderHistoryClass order =  dataSnapshot.getValue( OrderHistoryClass.class);
                    displayOrderList = order.getOrder();
                    displayPrice = order.getPrice();
                    itemList.setText(displayOrderList);
                    totalCost.setText(displayPrice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Display the credit cards from the database
    public void displayCreditCardList(){
        myRef = myFB.getReference("MunchiesDB").child("CustomerCreditCard").child(customerEmail);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    showCreditCardData(dataSnapshot);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Account cant make a payment without a valid credit card", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(CustomerPayment.this, CustomerCreditCard.class);
                    startActivity(myIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showCreditCardData(DataSnapshot dataSnapshot){
        customerCreditCardArrayList.clear();
        for(DataSnapshot cards: dataSnapshot.getChildren()){
            CreditCardClass creditCard = cards.getValue( CreditCardClass.class);
            customerCreditCardArrayList.add(creditCard.getCreditCardNumber().toString());
        }

        customerCreditCardAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, customerCreditCardArrayList);
        creditCard.setAdapter(customerCreditCardAdapter);

    }

    public void ProceedPayment(){
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUser = customerEmail.toString();
                String getItemList = itemList.getText().toString();
                String getCreditCard = creditCard.getSelectedItem().toString();
                String getPrice = totalCost.getText().toString();

                if(getCreditCard.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Account cant make a payment without a valid credit card", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(CustomerPayment.this, Login.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }




}