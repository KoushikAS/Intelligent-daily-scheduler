package com.example.android.intellischeduler;

/**
 * Created by AKANKSH on 3/9/2018.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    //EditText editTextemail,editTextpassword;
    private Button buttonsignin;
    private EditText editTextemail;
    private EditText editTextpassword;
    private TextView textViewsignup;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextemail=(EditText) findViewById(R.id.editTextemail);
        editTextpassword=(EditText)findViewById(R.id.editTextpassword);
        buttonsignin=(Button) findViewById(R.id.buttonsignin);
        textViewsignup=(TextView) findViewById(R.id.textViewsignup);
        progressDialog=new ProgressDialog(this) ;
        buttonsignin.setOnClickListener(this);
        textViewsignup.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){

            finish();
            startActivity(new Intent(this,TabWithIconActivity.class));
        }
        //findViewById(R.id.button5).setOnClickListener(this);
    }

    public void userLogin() {
        String email=editTextemail.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6) {
            //Toast.makeText(this, "password should be of length 6", Toast.LENGTH_SHORT).show();
            editTextpassword.setError("password should be minimum of length 6");
            editTextpassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("please enter valid email address");
            editTextemail.requestFocus();
            return;
        }
        progressDialog.setMessage("Logging In ..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){

                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, " Could not signin", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public void onClick(View view){
        if(view==buttonsignin){
            userLogin();

        }
        if(view==textViewsignup){
            finish();
            startActivity(new Intent(this,SignUpActivity.class));
        }

    }
}


