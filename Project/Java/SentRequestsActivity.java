package com.example.projectcompanionfinder;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class SentRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerSentRequests;
    ArrayList<String> sentList;
    SentRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_requests);

        recyclerSentRequests = findViewById(R.id.recyclerSentRequests);
        recyclerSentRequests.setLayoutManager(new LinearLayoutManager(this));

        sentList = new ArrayList<>();
        adapter = new SentRequestAdapter(this, sentList);
        recyclerSentRequests.setAdapter(adapter);

        loadSentRequests();
    }

    private void loadSentRequests() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("SentRequests")
                .child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                sentList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        sentList.add(data.getKey());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SentRequestsActivity.this,
                        "Failed to load sent requests",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}