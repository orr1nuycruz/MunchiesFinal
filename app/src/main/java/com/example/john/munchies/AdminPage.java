package com.example.john.munchies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {

    Button btnAddRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        btnAddRestaurant = (Button)findViewById(R.id.btnAddRestaurantRedirect);
        RedirectAddRestaurant();
    }
    public void RedirectAddRestaurant(){
        btnAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminPage.this, AdminAddRestaurant.class);
                startActivity(i);
            }
        });
    }
}
