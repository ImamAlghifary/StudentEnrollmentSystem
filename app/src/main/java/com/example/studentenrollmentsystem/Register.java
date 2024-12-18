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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private Button btnSignUp;
    private EditText txtEmail, txtPassword, txtConfirmPassword;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

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
                if (!password.equals(confirmPassword)) {
                    txtConfirmPassword.setError("Passwords do not match");
                    txtConfirmPassword.requestFocus();
                    return;
                }
                registerUser(email, password);
            }
        });
    }
    private void registerUser (String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = auth.getCurrentUser().getUid();
                    UsersModel newUser = new UsersModel(userId, email, 0, new ArrayList<>());
                    firestore.collection("users").document(userId).set(newUser)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Login.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Failed to save user to Firestore.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Registration failed.", Toast.LENGTH_SHORT).show();
                });
    }
}