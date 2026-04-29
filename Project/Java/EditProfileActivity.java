package com.example.projectcompanionfinder;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    TextInputEditText etProjectTitle, etProjectDesc, etProjectRoles;
    Button btnSaveProject;
    String currentUid;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etProjectTitle = findViewById(R.id.etProjectTitle);
        etProjectDesc = findViewById(R.id.etProjectDesc);
        etProjectRoles = findViewById(R.id.etProjectRoles);
        btnSaveProject = findViewById(R.id.btnSaveProject);

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUid);

        // Load existing details
        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String title = snapshot.child("projectTitle").getValue(String.class);
                String desc = snapshot.child("projectDesc").getValue(String.class);
                String roles = snapshot.child("projectRoles").getValue(String.class);

                if (title != null) etProjectTitle.setText(title);
                if (desc != null) etProjectDesc.setText(desc);
                if (roles != null) etProjectRoles.setText(roles);
            }
        });

        btnSaveProject.setOnClickListener(v -> saveProjectDetails());
    }

    private void saveProjectDetails() {
        String title = etProjectTitle.getText().toString().trim();
        String desc = etProjectDesc.getText().toString().trim();
        String roles = etProjectRoles.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etProjectTitle.setError("Required");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("projectTitle", title);
        updates.put("projectDesc", desc);
        updates.put("projectRoles", roles);

        userRef.updateChildren(updates).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Project details updated!", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
