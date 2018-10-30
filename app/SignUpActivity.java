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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private Button buttonregister;
    private EditText editTextemail;
    private EditText editTextpassword;
    private TextView textView3;
    private ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //

        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);
        firebaseAuth=FirebaseAuth.getInstance();



        if(firebaseAuth.getCurrentUser()!=null){

            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        progressDialog=new ProgressDialog(this);
        buttonregister=(Button) findViewById(R.id.buttonregister);
        editTextemail=(EditText) findViewById(R.id.editTextemail);
        editTextpassword=(EditText) findViewById(R.id.editTextpassword);
        //findViewById(R.id.textView3).setOnClickListener(this);
        textView3=(TextView) findViewById(R.id.textView3);

        buttonregister.setOnClickListener(this);
        textView3.setOnClickListener(this);
    }

    private void registerUser(){
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
        progressDialog.setMessage("Registering User..");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(Main3Activity.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(SignUpActivity.this, TabWithIconActivity.class));
                }
                else {
                    Toast.makeText(SignUpActivity.this, " Could not register", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }



    public void onClick(View view){
        if(view==buttonregister){
            registerUser();
        }
        if(view==textView3){
            //finish();
            startActivity(new Intent(this,LoginActivity.class));
            //userLogin();
        }
    }
}
