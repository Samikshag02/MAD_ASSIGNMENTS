package com.example.menu_with_event_handling;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.txtResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.profile) {
            txtResult.setText("Profile Selected");
            Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.settings) {
            txtResult.setText("Settings Selected");
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.about) {
            txtResult.setText("About Selected");
            Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.exit) {
            Toast.makeText(this, "Exiting App", Toast.LENGTH_SHORT).show();
            finish(); // closes app
        }

        return true;
    }
}
