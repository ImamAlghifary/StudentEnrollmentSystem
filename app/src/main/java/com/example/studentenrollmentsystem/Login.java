package com.example.studentenrollmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.List;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private Button btnLogin, btnRegister;
    private EditText txtEmail, txtPassword;
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

        auth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        firestore = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Email is required");
                    txtEmail.requestFocus();
                    return;
                }
                if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)) {
                    txtEmail.setError("Invalid email pattern");
                    txtEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("Password is required");
                    txtPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    txtPassword.setError("Password must be at least 6 characters long");
                    txtPassword.requestFocus();
                    return;
                }

                loginUser(email, password);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegister = new Intent(getBaseContext(), Register.class);
                startActivity(goToRegister);
            }
        });
    }
    private void loginUser(String email, String Password){
        auth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(authResult -> {
            String userId = auth.getCurrentUser().getUid();
            getUserDetails(userId);
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Login account failed", Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() != null) {
            if (auth.getCurrentUser() != null) {
                getUserDetails(auth.getCurrentUser().getUid());
            }
        }
    }

    private void getUserDetails(String userId){
        FirebaseFirestore.getInstance().collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Intent goToMain = new Intent(Login.this, MainActivity.class);
                        startActivity(goToMain);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "User data not found in Firestore.", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error checking user in Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}