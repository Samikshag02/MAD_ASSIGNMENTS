package com.example.projectcompanionfinder;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    ArrayList<User> userList;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = userList.get(position);

        // ✅ Safe data binding
        holder.tvName.setText(user.name != null ? user.name : "No Name");
        holder.tvBranch.setText(user.branch != null ? user.branch : "No Branch");
        holder.tvSkills.setText(user.skills != null ? user.skills : "No Skills");

        // ⭐ Skill Highlight
        String currentUserSkill = "java"; // (can be dynamic later)

        if (user.skills != null &&
                user.skills.toLowerCase().contains(currentUserSkill.toLowerCase())) {

            holder.tvSkills.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.tvSkills.setTextColor(Color.parseColor("#000000"));
        }

        // 🔥 Open Profile
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ProfileActivity.class);

            intent.putExtra("name", user.name);
            intent.putExtra("email", user.email);
            intent.putExtra("branch", user.branch);
            intent.putExtra("skills", user.skills);
            intent.putExtra("uid", user.uid); // Pass the UID

            context.startActivity(intent);
        });

        // 🤝 CONNECT BUTTON (FINAL VERSION)
        holder.btnConnect.setOnClickListener(v -> {

            String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receiverUid = user.uid;

            DatabaseReference requestRef = FirebaseDatabase.getInstance()
                    .getReference("ConnectionRequests");

            DatabaseReference sentRef = FirebaseDatabase.getInstance()
                    .getReference("SentRequests");

            // ❌ Prevent duplicate request
            if (senderUid.equals(receiverUid)) {
                Toast.makeText(context, "Cannot connect to yourself", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 Save request (receiver side)
            requestRef.child(receiverUid)
                    .child(senderUid)
                    .setValue("pending");

            // 🔥 Save sent request (sender side)
            sentRef.child(senderUid)
                    .child(receiverUid)
                    .setValue("pending")
                    .addOnSuccessListener(aVoid -> {

                        holder.btnConnect.setText("Requested");
                        holder.btnConnect.setEnabled(false);

                        Toast.makeText(context,
                                "Request sent to " + (user.name != null ? user.name : "User"),
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context,
                                "Failed to send request",
                                Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvBranch, tvSkills;
        Button btnConnect;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvBranch = itemView.findViewById(R.id.tvBranch);
            tvSkills = itemView.findViewById(R.id.tvSkills);
            btnConnect = itemView.findViewById(R.id.btnConnect);
        }
    }
}