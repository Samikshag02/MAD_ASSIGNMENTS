package com.example.my_layout_application;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView imgShow;
    Button btnNature, btnFood, btnAnimals;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = findViewById(R.id.tvTitle);
        imgShow = findViewById(R.id.imgShow);

        btnNature = findViewById(R.id.btnNature);
        btnFood = findViewById(R.id.btnFood);
        btnAnimals = findViewById(R.id.btnAnimals);

        mainLayout = findViewById(R.id.mainLayout);

        // 🌿 Nature
        btnNature.setOnClickListener(v -> {
            tvTitle.setText("Nature 🌿");
            imgShow.setImageResource(R.drawable.nature1);
            mainLayout.setBackgroundColor(Color.parseColor("#D1FAE5"));
        });

        // 🍕 Food
        btnFood.setOnClickListener(v -> {
            tvTitle.setText("Food 🍕");
            imgShow.setImageResource(R.drawable.food1);
            mainLayout.setBackgroundColor(Color.parseColor("#FFE4C4"));
        });

        // 🐶 Animals
        btnAnimals.setOnClickListener(v -> {
            tvTitle.setText("Animals 🐶");
            imgShow.setImageResource(R.drawable.animal1);
            mainLayout.setBackgroundColor(Color.parseColor("#F3F4F6"));
        });
    }
}