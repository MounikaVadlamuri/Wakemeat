package com.capstone.wakemeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    TextView signUpTextView;
    Button loginButton  ;
    EditText emailEditText;
    EditText passwordEditText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpTextView = findViewById(R.id.SignUp);

        signUpTextView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Login.this, Signup.class);
                        startActivity(i);
                    }
                }
        );

        loginButton = findViewById(R.id.LoginButton);
        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.Password);

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = emailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        String expectedEmail = "admin@admin.com";
                        String expectedPassword = "admin";

                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(Login.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (email.equals(expectedEmail) && password.equals(expectedPassword)) {
                            Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this,MapsInteraction.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }
}