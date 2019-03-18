package com.example.john.munchies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper myDB;
    EditText loginname, loginpass;

    String email, password;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Button register, login, changePassword, logout, next, GoAddRes, GotoRest;
    LinearLayout custView, admView;
    TextView txtlogin, txtpass, txtv;

    ProgressDialog progressDialog;

    //Firebase AUTH
    FirebaseAuth mAuth;
    // FirebaseAuth.AuthStateListener mAuthListner;



    //OnStart is way before the views
    @Override
    protected void onStart() {
        super.onStart();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //SQLITE Database
        myDB = new DatabaseHelper(this);

        //Firebase Database
        mAuth = FirebaseAuth.getInstance();

        //View
        loginname = (EditText) findViewById(R.id.editLoginUserID);
        loginpass = (EditText) findViewById(R.id.editLoginPassword);

        login = (Button)findViewById(R.id.btnLogin);
        register = (Button)findViewById(R.id.btnRegisterRedirect);
        logout = (Button)findViewById(R.id.logout);
        changePassword = (Button) findViewById((R.id.CheckRows));
        next = (Button) findViewById((R.id.next));
        GoAddRes = (Button) findViewById(R.id.addRes);
        GotoRest = (Button) findViewById(R.id.goRestaurant);

        txtlogin = (TextView)findViewById(R.id.txtLoginUser);
        txtpass = (TextView)findViewById(R.id.txtLoginPassword);
        txtv= (TextView)findViewById(R.id.txtloginMessage);

        admView = (LinearLayout) findViewById(R.id.adminView);
        custView = (LinearLayout) findViewById(R.id.customerView) ;

        progressDialog = new ProgressDialog(this);

        //View Config
        configView();


        //Listeners
        login.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        logout.setOnClickListener(this);
        register.setOnClickListener(this);
        next.setOnClickListener(this);
        GoAddRes.setOnClickListener(this);
        GotoRest.setOnClickListener(this);

         preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    editor = preferences.edit();



    }


    @Override

    public void onClick(View view) {
        if (view == login){
            Login();
        }
        if (view == changePassword){
            changePass();
        }
        if (view == register){
            register();
        }
        if (view == logout){
            logout();
        }
        if (view == next){
            nextPage();
        }
        if(view == GoAddRes){
            AddAdminRes();
        }
        if(view == GotoRest){
            RestaurantLogin();
        }

    }

    public void Login(){
        password = loginpass.getText().toString();
        email = loginname.getText().toString();
        progressDialog.setMessage("Processing Login Page");
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Correct Account", Toast.LENGTH_SHORT).show();

                   String val = email.substring(0, email.indexOf("@"));
                    editor.putString("userEmail", val);
                    editor.apply();

                    currentPage();
                    progressDialog.cancel();

                    //Successful
                }
                else{
                    Toast.makeText(Login.this, "You have entered a wrong account", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();

                    //failed
                }
                progressDialog.cancel();

            }
        });
    }

    public void configView(){

        if (mAuth.getCurrentUser() != null){
            if(mAuth.getCurrentUser().getEmail().equals("admin@myadmin.ca")) {
                register.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                loginname.setVisibility(View.INVISIBLE);
                loginpass.setVisibility(View.INVISIBLE);

                txtlogin.setVisibility(View.INVISIBLE);
                txtpass.setVisibility(View.INVISIBLE);

                custView.setVisibility(View.INVISIBLE);
                txtv.setText("Welcome Admin");

                GotoRest.setVisibility(View.INVISIBLE);
                logout.setVisibility(View.VISIBLE);

                GoAddRes.setVisibility(View.VISIBLE);
            }
            else{
                register.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                loginname.setVisibility(View.INVISIBLE);
                loginpass.setVisibility(View.INVISIBLE);

                txtlogin.setVisibility(View.INVISIBLE);
                txtpass.setVisibility(View.INVISIBLE);

                String myEmail = mAuth.getCurrentUser().getEmail();
                String kept = myEmail.substring(0, myEmail.indexOf("@"));
                txtv.setText("Welcome Back " + kept + "!");

                custView.setVisibility(View.VISIBLE);

                logout.setVisibility(View.VISIBLE);
                GotoRest.setVisibility(View.INVISIBLE);

                GoAddRes.setVisibility(View.INVISIBLE);
            }

        }

        else {
            register.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            loginname.setVisibility(View.VISIBLE);
            loginpass.setVisibility(View.VISIBLE);

            txtlogin.setVisibility(View.VISIBLE);
            txtpass.setVisibility(View.VISIBLE);

            txtv.setText("Login");

            custView.setVisibility(View.INVISIBLE);

            GotoRest.setVisibility(View.VISIBLE);
            GoAddRes.setVisibility(View.INVISIBLE);

            logout.setVisibility(View.INVISIBLE);
        }



    }


    public void logout(){
        mAuth.signOut();
        Intent i = new Intent(Login.this,Login.class);
        startActivity(i);
    }

    public void nextPage(){
        Intent i = new Intent(Login.this,Restaurant.class);
        startActivity(i);
    }

    public void currentPage(){
        Intent i = new Intent(Login.this,Login.class);
        startActivity(i);
    }

    public void changePass(){
        Intent i = new Intent(Login.this,ChangePassword.class);
        startActivity(i);
    }

    public void register (){
        Intent i = new Intent(Login.this, SearchRegistered.class);
        startActivity(i);
    }

    public void RestaurantLogin (){
        Intent i = new Intent(Login.this, RestaurantLogin.class);
        startActivity(i);
    }

    public void AddAdminRes (){
        Intent i = new Intent(Login.this, AdminAddRestaurant.class);
        startActivity(i);
    }
}
