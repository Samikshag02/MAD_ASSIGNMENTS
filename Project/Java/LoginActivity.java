package com.example.projectcompanionfinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // ✅ NEW
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    EditText etLoginEmail, etLoginPassword;
    Button btnLoginUser;
    CircleImageView ivLoginLogo;
    TextView tvRegister; // ✅ NEW

    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        // ✅ Auto-login
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // 🔗 Link Views
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLoginUser = findViewById(R.id.btnLoginUser);
        ivLoginLogo = findViewById(R.id.ivLoginLogo);
        tvRegister = findViewById(R.id.tvRegister); // ✅ NEW

        // 🔥 Animations
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_click);
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        ivLoginLogo.startAnimation(zoom);

        // 🔗 REGISTER LINK CLICK ✅
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // 🔘 LOGIN BUTTON
        btnLoginUser.setOnClickListener(v -> {

            v.startAnimation(scale);

            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            // 🔹 Validation
            if (TextUtils.isEmpty(email)) {
                etLoginEmail.setError("Enter Email");
                etLoginEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etLoginPassword.setError("Enter Password");
                etLoginPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                etLoginPassword.setError("Minimum 6 characters required");
                etLoginPassword.requestFocus();
                return;
            }

            btnLoginUser.setEnabled(false);

            // 🔹 Firebase Login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        btnLoginUser.setEnabled(true);

                        if (task.isSuccessful()) {

                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                            );

                        } else {

                            String error = task.getException() != null
                                    ? task.getException().getMessage()
                                    : "Login Failed";

                            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}