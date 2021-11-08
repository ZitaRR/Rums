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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseClassActivity {

    //Widgets
    private TextView register;
    private EditText email, password;
    private Button login;
    //Firebase
    private FirebaseAuth auth;
    private FirebaseUser fUser;

    @Override
    protected void onStart() {
        super.onStart();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        //Check for user existence
        if (fUser != null){
            super.init();
            startSomeActivity(HomeActivity.class);
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PersistantStorage.getInstance();
        //Instantiate Widgets
        register = findViewById(R.id.text_register);
        email    = findViewById(R.id.edit_login_mail);
        password = findViewById(R.id.edit_login_password);
        login    = findViewById(R.id.button_login);
        //Instantiate Firebase
        auth     = FirebaseAuth.getInstance();
        fUser    = FirebaseAuth.getInstance().getCurrentUser();

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
                startSomeActivity(RegisterActivity.class);
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                finish();
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startSomeActivity(HomeActivity.class);
//                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login failed, please check credentials",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
