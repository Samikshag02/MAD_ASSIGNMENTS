package com.example.assignment_5_implicitexplicitintent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure you have a simple layout file named activity_second.xml
        setContentView(R.layout.activity_second);
    }
}