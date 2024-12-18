package com.example.studentenrollmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends ArrayAdapter<SubjectModel> {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private int currentCredits;

    public SubjectAdapter (Context context, List<SubjectModel> subjects){
        super(context,0, subjects);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.subject_item, parent, false);
        }
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtCredit = convertView.findViewById(R.id.txtCredit);
        Button btnEnroll = convertView.findViewById(R.id.btnEnroll);
        SubjectModel subject = getItem(position);

        if (subject != null){
            txtTitle.setText(subject.getTitle());
            txtCredit.setText(String.valueOf(subject.getCredit()));
        }

        fetchCurrentCredits();

        btnEnroll.setOnClickListener(v -> {
            if (currentCredits + subject.getCredit() > 24) {
                Toast.makeText(getContext(), "You cannot enroll in more than 24 credits.", Toast.LENGTH_SHORT).show();
                return;
            }
            btnEnroll.setEnabled(false);

            String userId = auth.getCurrentUser().getUid();
            if (userId != null) {
                db.collection("users").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            UsersModel user = documentSnapshot.toObject(UsersModel.class);
                            if (user != null) {
                                // Ensure enrolledSubjects is not null
                                if (user.getEnrolledSubjects() == null) {
                                    user.setEnrolledSubjects(new ArrayList<>());
                                }

                                user.getEnrolledSubjects().add(subject.getSubjectId());
                                user.setCurrentCredits(user.getCurrentCredits() + subject.getCredit());

                                db.collection("users").document(userId).set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            currentCredits = user.getCurrentCredits();
                                            btnEnroll.setEnabled(true);
                                            Toast.makeText(getContext(), "Enrollment Successful", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            btnEnroll.setEnabled(true);
                                            Toast.makeText(getContext(), "Enrollment Failed. Try Again.", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            btnEnroll.setEnabled(true);
                            Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private void fetchCurrentCredits(){
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId != null) {
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        UsersModel user = documentSnapshot.toObject(UsersModel.class);
                        if (user != null) {
                            currentCredits = user.getCurrentCredits();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to fetch current credits", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
