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
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button register;
    private FirebaseAuth auth;
    private PersistantStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        storage = PersistantStorage.getInstance();
        username = findViewById(R.id.edit_username);
        email = findViewById(R.id.edit_register_email);
        password = findViewById(R.id.edit_password);
        confirmPassword = findViewById(R.id.edit_confirm_password);
        register = findViewById(R.id.button_register_user);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Används inte än men tanken är att den skall skickas med till profilen.
                String txtUsername = username.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                String txtConfirmPassword = confirmPassword.getText().toString();

                //Lösenords kontroll till en början sen om allt stämmer så körs metoden för att användaren registreras.
                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "not everything is filled in", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.equals(txtConfirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                } else if (txtConfirmPassword.length() < 5){
                    Toast.makeText(RegisterActivity.this, "Password is too short!", Toast.LENGTH_SHORT).show();
                } else{
                    registerUser(txtEmail, txtConfirmPassword);
                }
            }
        });
    }
    //Behöver mer av appen så man kan ta med & hantera datan, men detta är ju metoden som registrerar användaren i Firebase.
    private void registerUser(String email, String confirmPassword) {
        String username = this.username.getText().toString();

        auth.createUserWithEmailAndPassword(email, confirmPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser authUser = auth.getCurrentUser();
                    RumUser user = new RumUser(authUser.getUid());
                    user.setUsername(username);
                    user.setPassword(confirmPassword);
                    user.setEmail(email);
                    storage.getUsers().insert(user);
                    storage.getUsers().commit();

                    Toast.makeText(RegisterActivity.this, "Registration complete, signing in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }else{
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
