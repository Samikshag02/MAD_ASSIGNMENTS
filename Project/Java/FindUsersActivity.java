package com.example.projectcompanionfinder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class FindUsersActivity extends AppCompatActivity {

    RecyclerView recyclerUsers;
    EditText etSearch;

    ArrayList<User> userList, filteredList;
    UserAdapter userAdapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        recyclerUsers = findViewById(R.id.recyclerUsers);
        etSearch = findViewById(R.id.etSearch);

        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        filteredList = new ArrayList<>();

        userAdapter = new UserAdapter(this, filteredList);
        recyclerUsers.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // ✅ Loading message
        Toast.makeText(this, "Loading users...", Toast.LENGTH_SHORT).show();

        loadUsers();

        // 🔍 SEARCH FILTER
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filteredList.clear();

                for (User user : userList) {
                    if (user.name != null && user.skills != null) {
                        if (user.name.toLowerCase().contains(s.toString().toLowerCase()) ||
                                user.skills.toLowerCase().contains(s.toString().toLowerCase())) {

                            filteredList.add(user);
                        }
                    }
                }

                // ✅ Show message if no results
                if (filteredList.isEmpty()) {
                    Toast.makeText(FindUsersActivity.this,
                            "No users found",
                            Toast.LENGTH_SHORT).show();
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadUsers() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                userList.clear();

                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                for (DataSnapshot data : snapshot.getChildren()) {

                    User user = data.getValue(User.class);

                    // ✅ Hide current logged-in user
                    if (user != null && user.uid != null && !user.uid.equals(currentUid)) {
                        userList.add(user);
                    }
                }

                // Show all initially
                filteredList.clear();
                filteredList.addAll(userList);

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FindUsersActivity.this,
                        "Failed to load users",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}