package com.example.chat_app.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chat_app.MainActivity;
import com.example.chat_app.R;
import com.example.chat_app.password.ResetPasswordActivity;
import com.example.chat_app.signup.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private String email,password;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar=findViewById(R.id.progressBar);
    }

    public void tvSignupClick(View v){
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void btnLoginClick(View v){
        email=etEmail.getText().toString().trim();
        password=etPassword.getText().toString().trim();
        if(email.equals("")){
            etEmail.setError(getString(R.string.enter_email));

        } else if(password.equals("")){
            etPassword.setError(getString(R.string.enter_password));
        } else{
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        //Toast.makeText(LoginActivity.this, "Udało się zalogować!",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Nie udało się zalogować: "+
                                task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    public void tvResetPasswordClick(View view){
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }
}