package com.capstone.wakemeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signup extends AppCompatActivity {

    Button signUpButton  ;
    EditText emailEditText;
    EditText passwordEditText ;
    EditText confirmPasswordEditText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpButton = findViewById(R.id.SignUpButton);
        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.Password);
        confirmPasswordEditText = findViewById(R.id.Confirm_Password);

        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = emailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        String confirmPassword = confirmPasswordEditText.getText().toString();

                        if (email.isEmpty() || password.isEmpty()|| confirmPassword.isEmpty()) {
                            Toast.makeText(Signup.this, "All fields must be filled in order to register a new user", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (password.equals(confirmPassword)) {
                            Toast.makeText(Signup.this, "User registered with success!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Signup.this,Login.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Signup.this, "Passwords must be equal, please review the passwords informed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }
}