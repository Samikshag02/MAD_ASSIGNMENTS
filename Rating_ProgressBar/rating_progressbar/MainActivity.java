package com.example.rating_progressbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RatingBar ratingBar;
    ProgressBar progressBar;
    Button btnSubmit;
    TextView txtRating, txtProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingBar = findViewById(R.id.ratingBar);
        progressBar = findViewById(R.id.progressBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtRating = findViewById(R.id.txtRating);
        txtProgress = findViewById(R.id.txtProgress);

        btnSubmit.setOnClickListener(v -> {

            int rating = (int) ratingBar.getRating();

            // Update Text
            txtRating.setText("Rating: " + rating);
            txtProgress.setText("Progress: " + rating + "/5");

            // Update ProgressBar
            progressBar.setProgress(rating);

            // Show Toast
            Toast.makeText(this, "You rated: " + rating, Toast.LENGTH_SHORT).show();
        });
    }
}