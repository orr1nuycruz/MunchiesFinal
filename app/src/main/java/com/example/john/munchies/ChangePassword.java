package com.example.john.munchies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper DB;
    EditText username, oldpass, newpass;
    Button changePass;
    ProgressDialog progressDialog;
    String email;

    //Firebase AUTH
    FirebaseAuth mAuth;
    FirebaseUser user;

    String getUser, getold, getnew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //SQLITE Database
        DB = new DatabaseHelper(this);

        //Firebase Database
        mAuth = FirebaseAuth.getInstance();

        //Views

        oldpass = (EditText) findViewById(R.id.editOldPass);
        newpass = (EditText) findViewById(R.id.editNewPass);
        changePass = (Button) findViewById(R.id.change);

        progressDialog = new ProgressDialog(this);


        //Listeners

        changePass.setOnClickListener(this);
    }

    @Override

    public void onClick(View view) {
        if (view == changePass){
            changePass();
        }


    }

    public void changePass(){

        //Firebase User
        user =  mAuth.getCurrentUser();
        if(user != null){

            //Get Values
            email = user.getEmail();
            String oldPassword = oldpass.getText().toString().trim();
            final String newPassword = newpass.getText().toString().trim();

            //Check if email aligns with password
            AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);
            progressDialog.setMessage("Changing Password...");
            progressDialog.show();
            // Prompt the user to provide their sign-in credentials USING the Current user's info
            user.reauthenticate(credential).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        //update the password

                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    Login();

                                }
                                else{
                                    Toast.makeText(ChangePassword.this, "Password Failed to Update", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        progressDialog.cancel();

                    }
                    else{

                        Toast.makeText(ChangePassword.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();

                    }
                    progressDialog.cancel();


                }
            });


            Toast.makeText(this, "Logged In as " + email, Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this, "NOT LOGGED In " , Toast.LENGTH_SHORT).show();
            Login();



        }

    }

    public void ChangePasswordDB(){
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser = username.getText().toString();
                getold = oldpass.getText().toString();
                getnew = newpass.getText().toString();
                Cursor cursor = DB.GetUser(getUser, getold);
                if(getUser.equals("") || getold.equals("") || getnew.equals("")){
                    Toast.makeText(ChangePassword.this, "Please fill in your info", Toast.LENGTH_LONG).show();
                }
                else{
                    if(cursor.getCount() < 1){
                        username.setText("");
                        oldpass.setText("");
                        newpass.setText("");
                        Toast.makeText(ChangePassword.this, "User does not exist or incorrect info", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Boolean isChanged = DB.UpdatePassword(getUser,getold,getnew);
                        if(isChanged == true){
                            username.setText("");
                            oldpass.setText("");
                            newpass.setText("");
                            Toast.makeText(ChangePassword.this, "User change password successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ChangePassword.this, Login.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(ChangePassword.this, "Password change unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });
    }

    public void Login(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

}
