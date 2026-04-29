package com.example.projectcompanionfinder;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ConnectionsActivity extends AppCompatActivity {

    RecyclerView recyclerConnections;
    ArrayList<String> connectionList;
    ConnectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        recyclerConnections = findViewById(R.id.recyclerConnections);
        recyclerConnections.setLayoutManager(new LinearLayoutManager(this));

        connectionList = new ArrayList<>();
        adapter = new ConnectionAdapter(this, connectionList);
        recyclerConnections.setAdapter(adapter);

        loadConnections();
    }

    private void loadConnections() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Connections")
                .child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                connectionList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        connectionList.add(data.getKey());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ConnectionsActivity.this,
                        "Failed to load connections",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}