package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUser extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button register;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edit_register_email);
        password = findViewById(R.id.edit_password);
        confirmPassword = findViewById(R.id.edit_confirm_password);
        register = findViewById(R.id.button_register_user);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                String txtConfirmPassword = confirmPassword.getText().toString();

                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtConfirmPassword)) {
                    Toast.makeText(RegisterUser.this, "not everything is filled in", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.equals(txtConfirmPassword)){
                    Toast.makeText(RegisterUser.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                } else if (txtConfirmPassword.length() < 5){
                    Toast.makeText(RegisterUser.this, "Password is too short!", Toast.LENGTH_SHORT).show();
                } else{
                    registerUser(txtEmail, txtConfirmPassword);
                }
            }
        });
    }
    private void registerUser(String email, String confirmPassword) {
        auth.createUserWithEmailAndPassword(email, confirmPassword).addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterUser.this, "Registration complete, signing in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterUser.this, MainActivity.class));
                }else{
                    Toast.makeText(RegisterUser.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
