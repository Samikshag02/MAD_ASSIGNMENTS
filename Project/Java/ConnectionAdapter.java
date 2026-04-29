package com.example.projectcompanionfinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ViewHolder> {

    Context context;
    ArrayList<String> uidList;

    public ConnectionAdapter(Context context, ArrayList<String> uidList) {
        this.context = context;
        this.uidList = uidList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvBranch, tvSkills;
        Button btnRemove;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvBranch = view.findViewById(R.id.tvBranch);
            tvSkills = view.findViewById(R.id.tvSkills);
            btnRemove = view.findViewById(R.id.btnRemove);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.connection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String userId = uidList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 🔥 Fetch full user data
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.exists()) {

                        String name = snapshot.child("name").getValue(String.class);
                        String branch = snapshot.child("branch").getValue(String.class);
                        String skills = snapshot.child("skills").getValue(String.class);

                        holder.tvName.setText("Name: " + (name != null ? name : ""));
                        holder.tvBranch.setText("Branch: " + (branch != null ? branch : ""));
                        holder.tvSkills.setText("Skills: " + (skills != null ? skills : ""));
                    }
                });

        // Click to Open Profile (which then has Message button)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("uid", userId);
            context.startActivity(intent);
        });

        // ❌ Remove connection
        holder.btnRemove.setOnClickListener(v -> {

            FirebaseDatabase.getInstance()
                    .getReference("Connections")
                    .child(currentUid)
                    .child(userId)
                    .removeValue();

            FirebaseDatabase.getInstance()
                    .getReference("Connections")
                    .child(userId)
                    .child(currentUid)
                    .removeValue();

            Toast.makeText(context, "Connection removed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return uidList.size();
    }
}
