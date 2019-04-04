package com.example.john.munchies;
//Harvey Cabrias
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

    Button btnDeleteCreditCard;
    Button btnAddCreditCard;
    Button btnCreditCardDetail;

    String userEmail;
    String creditNum;
    String customerEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_credit_card_list);

        myFB = FirebaseDatabase.getInstance();

        customerCreditCardArrayList = new ArrayList<String>();

        btnDeleteCreditCard = (Button)findViewById(R.id.btnDeleteCardList);
        btnAddCreditCard = (Button)findViewById(R.id.btnAddCreditCard);
        btnCreditCardDetail = (Button)findViewById(R.id.btnCreditCardDetail);
        btnDeleteCreditCard.setEnabled(false);
        btnCreditCardDetail.setEnabled(false);

        customerCreditCardList = (ListView)findViewById(R.id.customerCreditCardList);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = sharedPref.getString("customerEmail", "");
        displayCreditCardList();
        deleteCreditCard();
        MyIntent();
    }

    public void deleteCreditCard(){
        final CreditCardClass creditCard = new CreditCardClass();
        customerCreditCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                creditCard.setCreditCardID(customerCreditCardArrayList.get(position));
                creditNum = customerCreditCardArrayList.get(position).toString();
                btnDeleteCreditCard.setEnabled(true);
                btnCreditCardDetail.setEnabled(true);
                btnAddCreditCard.setEnabled(false);
            }
        });

        btnDeleteCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = creditCard.getCreditCardID();
                //Toast.makeText(getApplicationContext(), "Deleted " +  name  + " item menu", Toast.LENGTH_SHORT).show();
                if(id.equals("")){
                    Toast.makeText( CustomerCreditCardList.this, "Please Select credit card you before delete!", Toast.LENGTH_LONG).show();
                } else {
                    btnDeleteCreditCard.setEnabled(false);
                    btnCreditCardDetail.setEnabled(false);
                    btnAddCreditCard.setEnabled(true);
                    myRef.child(customerEmail).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myRef.child(id).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    public void MyIntent(){
        btnAddCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CustomerCreditCardList.this, CustomerCreditCard.class);
                startActivity(myIntent);
                btnDeleteCreditCard.setEnabled(false);
                btnCreditCardDetail.setEnabled(false);
            }
        });

        btnCreditCardDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CreditCardNumber", creditNum);
                editor.apply();

                Intent myIntent = new Intent(CustomerCreditCardList.this, CustomerCreditCardDetail.class);
                startActivity(myIntent);
            }
        });
    }


    //Display the restaurant name from the database
    public void displayCreditCardList(){
        String getCustomerCreditCardEmail = userEmail.toString();
        customerEmail = getCustomerCreditCardEmail.substring(0, getCustomerCreditCardEmail.indexOf("@"));

        myRef = myFB.getReference("MunchiesDB").child("CustomerCreditCard").child(customerEmail);
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
            CreditCardClass creditCard = cards.getValue( CreditCardClass.class);
            customerCreditCardArrayList.add(creditCard.getCreditCardNumber().toString());
        }

        customerCreditCardAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, customerCreditCardArrayList);
        customerCreditCardList.setAdapter(customerCreditCardAdapter);

    }
}
