package com.example.projectcompanionfinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvBranch, tvSkills;
    TextView tvProjectTitle, tvProjectDesc, tvProjectRoles;
    CircleImageView ivProfileDetail;
    View viewStatus;
    Button btnSendRequest, btnMessage;
    String receiverUid, currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 🔗 Initializing Views
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvBranch = findViewById(R.id.tvProfileBranch);
        tvSkills = findViewById(R.id.tvProfileSkills);
        tvProjectTitle = findViewById(R.id.tvProjectTitle);
        tvProjectDesc = findViewById(R.id.tvProjectDesc);
        tvProjectRoles = findViewById(R.id.tvProjectRoles);
        ivProfileDetail = findViewById(R.id.ivProfileDetail);
        viewStatus = findViewById(R.id.viewStatus);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        btnMessage = findViewById(R.id.btnMessage);

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUid = getIntent().getStringExtra("uid");

        if (receiverUid == null) {
            receiverUid = getIntent().getStringExtra("userId");
        }

        if (receiverUid != null) {
            fetchFullUserDetails();
            checkRelationship();
            observeStatus();
        }

        // ✨ Animation
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_click);

        // 🤝 Button Actions
        btnSendRequest.setOnClickListener(v -> {
            v.startAnimation(scale);
            sendConnectionRequest();
        });

        btnMessage.setOnClickListener(v -> {
            v.startAnimation(scale);
            Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
            intent.putExtra("userId", receiverUid);
            startActivity(intent);
        });
    }

    private void fetchFullUserDetails() {
        FirebaseDatabase.getInstance().getReference("Users").child(receiverUid)
                .get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String branch = snapshot.child("branch").getValue(String.class);
                        String skills = snapshot.child("skills").getValue(String.class);
                        String projectTitle = snapshot.child("projectTitle").getValue(String.class);
                        String projectDesc = snapshot.child("projectDesc").getValue(String.class);
                        String projectRoles = snapshot.child("projectRoles").getValue(String.class);

                        tvName.setText("Name: " + (name != null ? name : "N/A"));
                        tvEmail.setText("Email: " + (email != null ? email : "N/A"));
                        tvBranch.setText("Branch: " + (branch != null ? branch : "N/A"));
                        tvSkills.setText("Skills: " + (skills != null ? skills : "N/A"));
                        
                        tvProjectTitle.setText("Title: " + (projectTitle != null ? projectTitle : "N/A"));
                        tvProjectDesc.setText("Description: " + (projectDesc != null ? projectDesc : "N/A"));
                        tvProjectRoles.setText("Roles: " + (projectRoles != null ? projectRoles : "N/A"));
                    }
                });
    }

    private void checkRelationship() {
        if (currentUid.equals(receiverUid)) {
            btnSendRequest.setVisibility(View.GONE);
            btnMessage.setVisibility(View.GONE);
            return;
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        
        rootRef.child("Connections").child(currentUid).child(receiverUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            btnMessage.setVisibility(View.VISIBLE);
                            btnSendRequest.setVisibility(View.GONE);
                        } else {
                            rootRef.child("SentRequests").child(currentUid).child(receiverUid)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                btnSendRequest.setVisibility(View.VISIBLE);
                                                btnSendRequest.setText("Requested");
                                                btnSendRequest.setEnabled(false);
                                                btnMessage.setVisibility(View.GONE);
                                            } else {
                                                btnSendRequest.setVisibility(View.VISIBLE);
                                                btnSendRequest.setText("Send Connection Request");
                                                btnSendRequest.setEnabled(true);
                                                btnMessage.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {}
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void observeStatus() {
        FirebaseDatabase.getInstance().getReference("Users").child(receiverUid).child("status")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String status = snapshot.getValue(String.class);
                        if ("online".equals(status)) {
                            viewStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                        } else {
                            viewStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#808080")));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void sendConnectionRequest() {
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("ConnectionRequests");
        DatabaseReference sentRef = FirebaseDatabase.getInstance().getReference("SentRequests");

        requestRef.child(receiverUid).child(currentUid).setValue("pending");
        sentRef.child(currentUid).child(receiverUid).setValue("pending")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request sent!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}