package com.example.john.munchies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerCreditCardDetail extends AppCompatActivity {


    FirebaseDatabase myFB;
    DatabaseReference myRef;

    TextView creditHolderName;
    TextView creditNumber;
    TextView creditDate;
    TextView creditCVV;
    TextView creditEmail;

    String userEmail;
    String creditNum;
    String displayCustomerHolderName, displayCustomerCreditNum,
            displayCustomerCreditDate, displayCustomerCreditCVV,
            displayCustomerCreditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_credit_card_detail);

        myFB = FirebaseDatabase.getInstance();

        creditHolderName = (TextView)findViewById(R.id.displayHolderName);
        creditNumber = (TextView)findViewById(R.id.displayCreditCardNum);
        creditDate = (TextView)findViewById(R.id.displayCreditDate);
        creditCVV = (TextView)findViewById(R.id.displayCreditCVVCode);
        creditEmail = (TextView)findViewById(R.id.displayCustomerEmail);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = sharedPref.getString("customerEmail", "");
        creditNum = sharedPref.getString("CreditCardNumber", "");


        displayCreditDetails();
    }

    public void displayCreditDetails(){

        String getCustomerCreditCardEmail = userEmail.toString();
        String customerEmail = getCustomerCreditCardEmail.substring(0, getCustomerCreditCardEmail.indexOf("@"));
        myRef = myFB.getReference("MunchiesDB").child("CustomerCreditCard").child(customerEmail).child(creditNum);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    CreditCardClass creditCard = dataSnapshot.getValue( CreditCardClass.class);
                    displayCustomerHolderName = creditCard.getHolderName();
                    displayCustomerCreditNum = creditCard.getCreditCardNumber();
                    displayCustomerCreditDate = creditCard.getDate();
                    displayCustomerCreditCVV = Integer.toString(creditCard.getCvv());
                    displayCustomerCreditEmail = creditCard.getEmail();
                    creditHolderName.setText(displayCustomerHolderName);
                    creditNumber.setText(displayCustomerCreditNum);
                    creditDate.setText(displayCustomerCreditDate);
                    creditCVV.setText(displayCustomerCreditCVV);
                    creditEmail.setText(displayCustomerCreditEmail);

                } else {
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
