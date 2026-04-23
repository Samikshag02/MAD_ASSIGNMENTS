package com.example.assignment_8_menus;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        tvDescription = findViewById(R.id.tvDescription);
    }

    // This method loads the menu into the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }

    // This method handles the click events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_java) {
            tvDescription.setText("Java: A high-level, class-based, object-oriented programming language designed to have as few implementation dependencies as possible.");
            return true;
        }
        else if (id == R.id.menu_python) {
            tvDescription.setText("Python: An interpreted, high-level, general-purpose programming language emphasized on code readability.");
            return true;
        }
        else if (id == R.id.menu_cpp) {
            tvDescription.setText("C++: A powerful general-purpose programming language used to develop operating systems, browsers, and games.");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}