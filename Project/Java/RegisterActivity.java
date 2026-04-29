package com.example.projectcompanionfinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etBranch, etSkills, etPassword, etDivision;
    Spinner spAcademicYear;
    Button btnRegisterUser;
    TextView tvLogin;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // 🔗 Link Views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etBranch = findViewById(R.id.etBranch);
        etSkills = findViewById(R.id.etSkills);
        etPassword = findViewById(R.id.etPassword);
        etDivision = findViewById(R.id.etDivision);

        spAcademicYear = findViewById(R.id.spAcademicYear);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);
        tvLogin = findViewById(R.id.tvLogin);

        // 🔥 Spinner Data (BOLD + CLEAR)
        String[] years = {"Select Year", "First Year", "Second Year", "Third Year", "Final Year"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                years
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(getResources().getColor(android.R.color.black));
                view.setTextSize(16);
                view.setTypeface(null, android.graphics.Typeface.BOLD); // 🔥 BOLD TEXT
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAcademicYear.setAdapter(adapter);

        // 🔗 Login Link
        tvLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );

        // 🔥 Animation
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_click);

        btnRegisterUser.setOnClickListener(v -> {

            v.startAnimation(scale);

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String branch = etBranch.getText().toString().trim();
            String skills = etSkills.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String division = etDivision.getText().toString().trim();
            String year = spAcademicYear.getSelectedItem().toString();

            // 🔹 Validation
            if (TextUtils.isEmpty(name)) {
                etName.setError("Enter Name");
                etName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Enter Email");
                etEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Enter Password");
                etPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                etPassword.setError("Minimum 6 characters required");
                etPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(branch)) {
                etBranch.setError("Enter Branch");
                etBranch.requestFocus();
                return;
            }

            if (year.equals("Select Year")) {
                Toast.makeText(this, "Select Academic Year", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(division)) {
                etDivision.setError("Enter Division");
                etDivision.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(skills)) {
                etSkills.setError("Enter Skills");
                etSkills.requestFocus();
                return;
            }

            btnRegisterUser.setEnabled(false);

            // 🔥 Firebase Registration
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            String uid = mAuth.getCurrentUser().getUid();

                            User user = new User(
                                    name,
                                    email,
                                    branch,
                                    skills,
                                    uid,
                                    "",
                                    year,
                                    division
                            );

                            databaseReference.child(uid).setValue(user)
                                    .addOnSuccessListener(unused -> {

                                        btnRegisterUser.setEnabled(true);

                                        Toast.makeText(this,
                                                "Registration Successful",
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(this, LoginActivity.class));
                                        finish();
                                    });

                        } else {

                            btnRegisterUser.setEnabled(true);

                            Exception e = task.getException();

                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(this,
                                        "Email already registered",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this,
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });
    }
}