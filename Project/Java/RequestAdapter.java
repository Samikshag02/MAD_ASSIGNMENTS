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

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;
    ArrayList<String> senderList;

    public RequestAdapter(Context context, ArrayList<String> senderList) {
        this.context = context;
        this.senderList = senderList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUser, tvBranch, tvSkills;
        Button btnAccept, btnReject;

        public ViewHolder(View view) {
            super(view);

            tvUser = view.findViewById(R.id.tvUser);
            tvBranch = view.findViewById(R.id.tvBranch);
            tvSkills = view.findViewById(R.id.tvSkills);

            btnAccept = view.findViewById(R.id.btnAccept);
            btnReject = view.findViewById(R.id.btnReject);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String senderUid = senderList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        String receiverUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 🔥 Fetch FULL USER DATA
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(senderUid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.exists()) {

                        String name = snapshot.child("name").getValue(String.class);
                        String branch = snapshot.child("branch").getValue(String.class);
                        String skills = snapshot.child("skills").getValue(String.class);

                        holder.tvUser.setText("Name: " + (name != null ? name : "User"));
                        holder.tvBranch.setText("Branch: " + (branch != null ? branch : "-"));
                        holder.tvSkills.setText("Skills: " + (skills != null ? skills : "-"));

                    } else {
                        holder.tvUser.setText("Unknown User");
                        holder.tvBranch.setText("");
                        holder.tvSkills.setText("");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.tvUser.setText("Error loading user");
                });

        // 🔥 Open Profile
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("uid", senderUid);
            context.startActivity(intent);
        });

        // ✅ ACCEPT
        holder.btnAccept.setOnClickListener(v -> {

            // Save connection both sides
            FirebaseDatabase.getInstance()
                    .getReference("Connections")
                    .child(receiverUid)
                    .child(senderUid)
                    .setValue("connected");

            FirebaseDatabase.getInstance()
                    .getReference("Connections")
                    .child(senderUid)
                    .child(receiverUid)
                    .setValue("connected");

            // Update sender status
            FirebaseDatabase.getInstance()
                    .getReference("SentRequests")
                    .child(senderUid)
                    .child(receiverUid)
                    .setValue("accepted");

            // Remove request
            FirebaseDatabase.getInstance()
                    .getReference("ConnectionRequests")
                    .child(receiverUid)
                    .child(senderUid)
                    .removeValue();

            Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
        });

        // ❌ REJECT
        holder.btnReject.setOnClickListener(v -> {

            FirebaseDatabase.getInstance()
                    .getReference("SentRequests")
                    .child(senderUid)
                    .child(receiverUid)
                    .setValue("rejected");

            FirebaseDatabase.getInstance()
                    .getReference("ConnectionRequests")
                    .child(receiverUid)
                    .child(senderUid)
                    .removeValue();

            Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return senderList.size();
    }
}