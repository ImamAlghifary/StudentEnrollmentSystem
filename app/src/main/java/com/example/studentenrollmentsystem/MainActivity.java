package com.example.studentenrollmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

private CardView btnEnrollment, btnLogOut, btnSummary;
private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnEnrollment = findViewById(R.id.btnEnrollment);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnSummary = findViewById(R.id.btnSummary);
        auth = FirebaseAuth.getInstance();

        btnEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEnroll = new Intent(getApplicationContext(), Enrollment.class);
                startActivity(goToEnroll);
            }
        });

        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSummary = new Intent(getApplicationContext(), Summary.class);
                startActivity(goToSummary);
            }
        });

        btnLogOut.setOnClickListener(view -> {
            auth.signOut();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });

    }
}