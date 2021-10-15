package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView register;
    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.text_register);
        email = findViewById(R.id.edit_login_mail);
        password = findViewById(R.id.edit_login_password);
        login = findViewById(R.id.button_login);
        auth = FirebaseAuth.getInstance();

        //Här ska det ju hända lite mer än vad som händer i nuläget, man bara loggas in som det är nu.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                loginUser(txtEmail, txtPassword);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    //
    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(LoginActivity.this, xxx.class));
            }

        });
    }
 }
/*
EJ TESTAD KOD:
ISTÄLLET FÖR OnSuccesListener
Behövs fortfarande mer av appen för att kunna hantera datan

    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            FirebaseUser user = auth.getCurrentUser();
            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(LoginActivity.this, "Login failed, make sure mail & pw is correct", Toast.LENGTH_SHORT).show();
        }

    }
});*/
