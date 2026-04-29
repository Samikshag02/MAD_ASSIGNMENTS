package com.example.projectcompanionfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private ArrayList<Message> mChat;

    public ChatAdapter(Context context, ArrayList<Message> mChat) {
        this.context = context;
        this.mChat = mChat;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_sent, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_received, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        // 🕒 Set Real Time
        if (chat.getTimestamp() != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String time = sdf.format(new Date(chat.getTimestamp()));
            holder.tvTime.setText(time);
        } else {
            holder.tvTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        String fUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (mChat.get(position).getSenderId().equals(fUserUid)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
