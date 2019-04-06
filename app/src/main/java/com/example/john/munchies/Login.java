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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper myDB;
    EditText loginname, loginpass;

    String email, password;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Button register, login, changePassword, logout, next,
            GoAddRes, creditCardInfo, viewCreditCard, ViewComp, viewFoodHistory;
    LinearLayout custView, admView;
    TextView txtlogin, txtpass, txtv, txtTimer;

    ProgressDialog progressDialog;

    //Firebase AUTH
    FirebaseAuth mAuth;
    // FirebaseAuth.AuthStateListener mAuthListner;
    String admin, timerdiff;
    Date initial = null,
            current = null;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference1;



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

        //Firebase Database - like a session - remove the instance & its new
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
        ViewComp = (Button) findViewById(R.id.viewComplaint);
        viewFoodHistory = (Button) findViewById(R.id.viewHistory);

        creditCardInfo = (Button)findViewById(R.id.addCreditCard);
        viewCreditCard = (Button)findViewById(R.id.viewCreditCard);

        txtlogin = (TextView)findViewById(R.id.txtLoginUser);
        txtpass = (TextView)findViewById(R.id.txtLoginPassword);
        txtv= (TextView)findViewById(R.id.txtloginMessage);
        txtTimer= (TextView)findViewById(R.id.timer);

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
        ViewComp.setOnClickListener(this);
        viewFoodHistory.setOnClickListener(this);
        creditCardInfo.setOnClickListener(this);
        viewCreditCard.setOnClickListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();




        //TESTING TIMER

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String cur = dateFormat.format(new Date());
        try{
           initial = dateFormat.parse("04/05/2019 19:30:05");
           current = dateFormat.parse(cur);
        }
        catch(Exception e){

        }
        long diff = current.getTime() - initial.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        txtTimer.setText(diffHours + ":" + diffMinutes);

    }


    @Override

    public void onClick(View view) {
        if (view == login){
            String getLoginName = loginname.getText().toString();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("customerEmail", getLoginName);
            editor.apply();
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
        if(view == ViewComp){
            ViewRestComplaints();
        }
        if(view == creditCardInfo){
            CustomerCreditCardInfo();
        }
        if(view == viewCreditCard ){
            ViewCustomerCreditCard();
        }
        if(view == viewFoodHistory){
            ViewHistoryPage();
        }

    }

    public void Login(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        password = loginpass.getText().toString();
        email = loginname.getText().toString();
        progressDialog.setMessage("Processing Login Page");
        progressDialog.show();
        if(email.equals("admin@myadmin.com") && password.equals("admin01")){
            String val = email.substring(0, email.indexOf("@"));
            editor.putString("userEmail", val);
            editor.apply();
            progressDialog.cancel();
            Toast.makeText(Login.this, "Correct Account", Toast.LENGTH_SHORT).show();
            currentPage();
        }
        else{
            //SIGNS IN WITH USER - GETS USER
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        String val = email.substring(0, email.indexOf("@"));
                        editor.putString("userEmail", val);
                        editor.apply();
                        progressDialog.cancel();


                    emailVerification();
                    Toast.makeText(Login.this, "Correct Account", Toast.LENGTH_SHORT).show();
                        emailVerification();
                        Toast.makeText(Login.this, "Correct Account", Toast.LENGTH_SHORT).show();




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
    }

    private void emailVerification() {
        //Meaning User Logged in
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if (emailflag == true) {
            finish();
            currentPage();
        } else {
            Toast.makeText(this, "Verify your Email", Toast.LENGTH_SHORT).show();
            mAuth.signOut();

        }
    }


    public void configView(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        admin = preferences.getString("userEmail", "");
        if(admin.equals("admin")){
            register.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
            loginname.setVisibility(View.INVISIBLE);
            loginpass.setVisibility(View.INVISIBLE);

            txtlogin.setVisibility(View.INVISIBLE);
            txtpass.setVisibility(View.INVISIBLE);

            custView.setVisibility(View.INVISIBLE);
            txtv.setText("Admin Login");

            logout.setVisibility(View.VISIBLE);
            admView.setVisibility(View.VISIBLE);
        }
        else{
            if (mAuth.getCurrentUser() != null){
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
                admView.setVisibility(View.INVISIBLE);

                logout.setVisibility(View.VISIBLE);

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
                admView.setVisibility(View.INVISIBLE);

                logout.setVisibility(View.INVISIBLE);
            }
        }

    }


    public void logout(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().remove("userEmail").commit();
        mAuth.signOut();
        Intent i = new Intent(Login.this,Login.class);
        startActivity(i);
    }

    public void nextPage(){
        Intent i = new Intent(Login.this,Restaurant.class);
        startActivity(i);
    }

    public void currentPage(){
        Intent i = new Intent(Login.this, Login.class);
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

    public void AddAdminRes (){
        Intent i = new Intent(Login.this, AdminAddRestaurant.class);
        startActivity(i);
    }
    public void CustomerCreditCardInfo(){
        Intent i = new Intent(Login.this, CustomerCreditCard.class);
        startActivity(i);
    }

    public void  ViewCustomerCreditCard(){
        Intent i = new Intent(Login.this, CustomerCreditCardList.class);
        startActivity(i);
    }

    public void ViewRestComplaints(){
        Intent i = new Intent(Login.this, ComplaintList.class);
        startActivity(i);
    }

    public void  ViewHistoryPage(){
        Intent i = new Intent(Login.this, ViewOrderHistory.class);
        startActivity(i);
    }
}


