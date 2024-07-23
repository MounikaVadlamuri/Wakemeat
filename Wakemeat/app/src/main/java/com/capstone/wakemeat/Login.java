package com.capstone.wakemeat;

import android.content.Intent;
import android.database.Cursor;
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
    DatabaseHelper dbHelper;

    TextView signUpTextView;
    Button loginButton  ;
    EditText emailEditText;
    EditText passwordEditText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DatabaseHelper(this);
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

                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(Login.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Cursor res = dbHelper.checkLogin(email, password);

                        if (res.getCount() > 0) {
                            Toast.makeText(Login.this, "Welcome aboard !", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this,MapsInteraction.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Login.this, "Invalid username or password! Are you sure you have signed up already?", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }
}