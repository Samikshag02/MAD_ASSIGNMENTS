package com.example.projectcompanionfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome, tvNameTop, tvEmail, tvBranch, tvSkills, tvYear, tvDivision;
    TextView tvProjectTitleHome, tvProjectDescHome, tvProjectRolesHome;
    CircleImageView ivProfileHome;

    Button btnFindUsers, btnLogout, btnRequests, btnConnections, btnSent, btnEditProject;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 🔗 Views
        ivProfileHome = findViewById(R.id.ivProfile);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvNameTop = findViewById(R.id.tvNameTop);
        tvEmail = findViewById(R.id.tvEmail);
        tvBranch = findViewById(R.id.tvBranch);
        tvSkills = findViewById(R.id.tvSkills);
        tvYear = findViewById(R.id.tvYear);
        tvDivision = findViewById(R.id.tvDivision);

        tvProjectTitleHome = findViewById(R.id.tvProjectTitleHome);
        tvProjectDescHome = findViewById(R.id.tvProjectDescHome);
        tvProjectRolesHome = findViewById(R.id.tvProjectRolesHome);

        btnFindUsers = findViewById(R.id.btnFindUsers);
        btnRequests = findViewById(R.id.btnRequests);
        btnConnections = findViewById(R.id.btnConnections);
        btnSent = findViewById(R.id.btnSent);
        btnEditProject = findViewById(R.id.btnEditProject);
        btnLogout = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_click);

        // 🔐 Login check
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        // 🔥 Fetch FULL user data
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.exists()) {

                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String branch = snapshot.child("branch").getValue(String.class);
                        String skills = snapshot.child("skills").getValue(String.class);
                        String year = snapshot.child("year").getValue(String.class);
                        String division = snapshot.child("division").getValue(String.class);
                        
                        String projectTitle = snapshot.child("projectTitle").getValue(String.class);
                        String projectDesc = snapshot.child("projectDesc").getValue(String.class);
                        String projectRoles = snapshot.child("projectRoles").getValue(String.class);

                        tvWelcome.setText("Welcome Back,");
                        tvNameTop.setText(name != null ? name : "User");
                        tvEmail.setText("Email: " + (email != null ? email : "N/A"));
                        tvBranch.setText("Branch: " + (branch != null ? branch : "N/A"));
                        tvSkills.setText("Skills: " + (skills != null ? skills : "N/A"));
                        tvYear.setText("Year: " + (year != null ? year : "N/A"));
                        tvDivision.setText("Division: " + (division != null ? division : "N/A"));

                        tvProjectTitleHome.setText(projectTitle != null ? projectTitle : "No Project Title");
                        tvProjectDescHome.setText(projectDesc != null ? projectDesc : "No description provided yet.");
                        tvProjectRolesHome.setText("Roles: " + (projectRoles != null ? projectRoles : "N/A"));

                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );

        // 🔹 Buttons
        btnFindUsers.setOnClickListener(v -> {
            v.startAnimation(scale);
            startActivity(new Intent(this, FindUsersActivity.class));
        });

        btnRequests.setOnClickListener(v -> {
            v.startAnimation(scale);
            startActivity(new Intent(this, RequestsActivity.class));
        });

        btnConnections.setOnClickListener(v -> {
            v.startAnimation(scale);
            startActivity(new Intent(this, ConnectionsActivity.class));
        });

        btnSent.setOnClickListener(v -> {
            v.startAnimation(scale);
            startActivity(new Intent(this, SentRequestsActivity.class));
        });

        btnEditProject.setOnClickListener(v -> {
            v.startAnimation(scale);
            startActivity(new Intent(this, EditProfileActivity.class));
        });

        // 🔥 Logout
        btnLogout.setOnClickListener(v -> {
            v.startAnimation(scale);
            updateStatus("offline");
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void updateStatus(String status) {
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("Users").child(uid).child("status").setValue(status);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus("offline");
    }
}