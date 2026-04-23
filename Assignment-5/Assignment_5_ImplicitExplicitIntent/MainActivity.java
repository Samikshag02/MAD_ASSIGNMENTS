package com.example.assignment_5_implicitexplicitintent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnExplicit = findViewById(R.id.btnExplicit);
        Button btnImplicit = findViewById(R.id.btnImplicit);

        // 1. EXPLICIT INTENT
        // Targets a specific class (SecondActivity)
        btnExplicit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        // 2. IMPLICIT INTENT
        // Declares an ACTION, letting the Android System decide which app handles it
        btnImplicit.setOnClickListener(v -> {
            String url = "https://www.google.com";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }
}