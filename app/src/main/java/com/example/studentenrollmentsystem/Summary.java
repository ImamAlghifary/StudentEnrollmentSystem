package com.example.studentenrollmentsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Summary extends AppCompatActivity {
    private ListView listViewEnrolled;
    private TextView txtTotalCredits;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewEnrolled = findViewById(R.id.listViewEnrolled);
        txtTotalCredits = findViewById(R.id.txtTotalCredits);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loadSummaryData();
    }
    private void loadSummaryData() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Long totalCredits = documentSnapshot.getLong("currentCredits");
                            List<String> enrolledSubjects = (List<String>) documentSnapshot.get("enrolledSubjects");
                            if (totalCredits != null) {
                                txtTotalCredits.setText(String.valueOf(totalCredits));
                            }
                            if (enrolledSubjects != null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Summary.this,
                                        android.R.layout.simple_list_item_1, enrolledSubjects);
                                listViewEnrolled.setAdapter(adapter);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    txtTotalCredits.setText("Error loading data");
                });
    }
}