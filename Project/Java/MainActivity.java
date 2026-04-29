package com.example.projectcompanionfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    FirebaseAuth mAuth;
    CircleImageView ivLogo; // ✅ declare here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // 🔥 Auto-login check
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // 🔗 Link Views
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        ivLogo = findViewById(R.id.ivLogo); // ✅ IMPORTANT

        // 🔥 Animation
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        ivLogo.startAnimation(zoom);

        // 🔘 Button Clicks
        btnLogin.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class))
        );
    }
}