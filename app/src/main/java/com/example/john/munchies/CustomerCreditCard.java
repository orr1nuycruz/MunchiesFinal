package com.example.john.munchies;
//Harvey Cabrias
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerCreditCard extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    Button btnAddCustomerCreditCard;

    EditText editCustomerHolderName;
    EditText editCustomerCreditCardNumber;
    EditText editCreditCardDate;
    EditText editCreditCardCVV;
    EditText editCustomerCreditCardEmail;

    String userEmail;
    Integer counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_credit_card);

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB").child("CustomerCreditCard");

        btnAddCustomerCreditCard = (Button)findViewById(R.id.btnAddCreditCardInfo);

        editCustomerHolderName = (EditText)findViewById(R.id.EditHolderName);
        editCustomerCreditCardNumber = (EditText)findViewById(R.id.EditCreditCardNumber);
        editCreditCardDate = (EditText)findViewById(R.id.EditCreditCardDate);
        editCreditCardCVV = (EditText)findViewById(R.id.EditCreditCardCVV);
        editCustomerCreditCardEmail = (EditText)findViewById(R.id.EditCustomerCreditCardEmail);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = sharedPref.getString("customerEmail", "");
        editCustomerCreditCardEmail.setText(userEmail);

        AddCustomerCreditCard();
    }

    public void AddCustomerCreditCard(){
        btnAddCustomerCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCustomerHolderName = editCustomerHolderName.getText().toString();
                String getCustomerCreditCardNum = editCustomerCreditCardNumber.getText().toString();
                String getCreditCardDate = editCreditCardDate.getText().toString();
                String getCreditCardCVV = editCreditCardCVV.getText().toString();
                String getCustomerCreditCardEmail = userEmail.toString();

                String customerEmail = getCustomerCreditCardEmail.substring(0, getCustomerCreditCardEmail.indexOf("@"));


                int creditCardCVV = Integer.parseInt(getCreditCardCVV);

                if(getCustomerHolderName.isEmpty() || getCustomerCreditCardNum.isEmpty() || getCreditCardDate.isEmpty() || getCreditCardCVV.isEmpty()){
                    Toast.makeText( CustomerCreditCard.this, "Please fill out required fields", Toast.LENGTH_LONG).show();
                }

                if(getCustomerHolderName.isEmpty()){
                    Toast.makeText( CustomerCreditCard.this, "Please fill out Holder Name", Toast.LENGTH_LONG).show();
                } else if (getCustomerCreditCardNum.isEmpty()){
                    Toast.makeText( CustomerCreditCard.this, "Please fill out Credit Card Number", Toast.LENGTH_LONG).show();
                } else if (getCreditCardDate.isEmpty()){
                    Toast.makeText( CustomerCreditCard.this, "Please fill out Credit Card Date", Toast.LENGTH_LONG).show();
                } else if (getCreditCardCVV.isEmpty()){
                    Toast.makeText( CustomerCreditCard.this, "Please fill out Credit CVV", Toast.LENGTH_LONG).show();
                } else if (getCustomerCreditCardNum.length() != 16){
                    Toast.makeText( CustomerCreditCard.this, "Please input 16 credit card digits", Toast.LENGTH_LONG).show();
                } else if (getCreditCardDate.length() != 4){
                    Toast.makeText( CustomerCreditCard.this, "Please enter 4 digits MM/YY", Toast.LENGTH_LONG).show();
                } else if (getCreditCardCVV.length() != 3){
                    Toast.makeText( CustomerCreditCard.this, "Please enter 3 CVV digits", Toast.LENGTH_LONG).show();
                }
                else {
                    counter =+ 1;
                    String getCustomerCreditCardID = Integer.toString(counter);
                    CreditCardClass creditCard = new CreditCardClass(getCustomerCreditCardID, getCustomerCreditCardNum, getCustomerHolderName, getCreditCardDate, creditCardCVV, getCustomerCreditCardEmail);
                    myRef.child(customerEmail).child(getCustomerCreditCardNum).setValue(creditCard);
                    editCustomerHolderName.setText("");
                    editCustomerCreditCardNumber.setText("");
                    editCreditCardDate.setText("");
                    editCreditCardCVV.setText("");

                    Intent myIntent = new Intent( CustomerCreditCard.this, CustomerCreditCardList.class);
                    startActivity(myIntent);
                }
            }
        });
    }
}
