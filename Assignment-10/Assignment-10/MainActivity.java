package com.example.assignment_10_internalstorage;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment; // Added for External Storage
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter; // Added
import java.io.File;           // Added
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;     // Added
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String INTERNAL_FILE = "my_history.txt";
    private static final String EXTERNAL_FILE_NAME = "MyVisibleData.txt";
    EditText etInput;
    TextView tvDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = findViewById(R.id.etInput);
        tvDisplay = findViewById(R.id.tvDisplay);
        Button btnSaveInternal = findViewById(R.id.btnSave); // Existing Internal Button
        Button btnLoad = findViewById(R.id.btnLoad);

        // Make sure you have a button in your XML with this ID
        Button btnSaveExternal = findViewById(R.id.btnSaveExternal);

        // Internal Storage Click
        btnSaveInternal.setOnClickListener(v -> saveData());

        // External Storage Click (The one you wanted visible)
        btnSaveExternal.setOnClickListener(v -> saveToExternalStorage());

        // Load Data (Internal)
        btnLoad.setOnClickListener(v -> loadData());
    }

    // --- INTERNAL STORAGE LOGIC ---
    private void saveData() {
        String text = etInput.getText().toString() + "\n";
        try (FileOutputStream fos = openFileOutput(INTERNAL_FILE, Context.MODE_APPEND)) {
            fos.write(text.getBytes());
            etInput.getText().clear();
            Toast.makeText(this, "Saved Internally", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- EXTERNAL STORAGE LOGIC (VISIBLE IN DOWNLOADS) ---
    private void saveToExternalStorage() {
        String text = etInput.getText().toString() + "\n";

        // Get the Public Downloads Directory
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, EXTERNAL_FILE_NAME);

        try {
            if (!path.exists()) {
                path.mkdirs();
            }

            // 'true' enables Append mode
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(text);
            bw.close();
            fw.close();

            etInput.getText().clear();
            Toast.makeText(this, "Saved to Downloads folder!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        try (FileInputStream fis = openFileInput(INTERNAL_FILE);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            tvDisplay.setText(sb.toString());
        } catch (Exception e) {
            tvDisplay.setText("No internal data found.");
        }
    }
}