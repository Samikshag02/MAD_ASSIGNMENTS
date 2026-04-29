package com.example.projectcompanionfinder;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.ViewHolder> {

    Context context;
    ArrayList<String> list;

    public SentRequestAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvBranch, tvSkills;
        View viewStatus;
        Button btnCancel;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvBranch = view.findViewById(R.id.tvBranch);
            tvSkills = view.findViewById(R.id.tvSkills);
            viewStatus = view.findViewById(R.id.viewStatus);
            btnCancel = view.findViewById(R.id.btnCancel);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.sent_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String userId = list.get(position);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 🔥 Fetch user details & Status (Real-time)
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String name = snapshot.child("name").getValue(String.class);
                            String branch = snapshot.child("branch").getValue(String.class);
                            String skills = snapshot.child("skills").getValue(String.class);
                            String status = snapshot.child("status").getValue(String.class);

                            holder.tvName.setText("Name: " + name);
                            holder.tvBranch.setText("Branch: " + branch);
                            holder.tvSkills.setText("Skills: " + skills);

                            // Update Status Dot
                            if ("online".equals(status)) {
                                holder.viewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                            } else {
                                holder.viewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        // 🔥 Open Profile
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("uid", userId);
            context.startActivity(intent);
        });

        // ❌ CANCEL REQUEST
        holder.btnCancel.setOnClickListener(v -> {
            FirebaseDatabase.getInstance()
                    .getReference("SentRequests")
                    .child(currentUid)
                    .child(userId)
                    .removeValue();

            FirebaseDatabase.getInstance()
                    .getReference("ConnectionRequests")
                    .child(userId)
                    .child(currentUid)
                    .removeValue();

            Toast.makeText(context, "Request Cancelled", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
