package com.example.projectcompanionfinder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerChat;
    private EditText etMessage;
    private ImageView btnSendMessage;
    private TextView tvChatName, tvTypingStatus;

    private String receiverUid;
    private String senderUid;

    private ChatAdapter chatAdapter;
    private ArrayList<Message> mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiverUid = getIntent().getStringExtra("userId");
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerChat = findViewById(R.id.recyclerChat);
        etMessage = findViewById(R.id.etMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        tvChatName = findViewById(R.id.tvChatName);
        tvTypingStatus = findViewById(R.id.tvTypingStatus);

        recyclerChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerChat.setLayoutManager(linearLayoutManager);

        // Fetch Receiver Name
        FirebaseDatabase.getInstance().getReference("Users").child(receiverUid)
                .get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        tvChatName.setText(name);
                    }
                });

        btnSendMessage.setOnClickListener(v -> {
            String msg = etMessage.getText().toString();
            if (!msg.isEmpty()) {
                sendMessage(senderUid, receiverUid, msg);
            } else {
                Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
            etMessage.setText("");
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    setTypingStatus(false);
                } else {
                    setTypingStatus(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        readMessages(senderUid, receiverUid);
        observeTypingStatus();
    }

    private void setTypingStatus(boolean isTyping) {
        FirebaseDatabase.getInstance().getReference("TypingStatus")
                .child(senderUid)
                .child(receiverUid)
                .setValue(isTyping);
    }

    private void observeTypingStatus() {
        FirebaseDatabase.getInstance().getReference("TypingStatus")
                .child(receiverUid)
                .child(senderUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean isTyping = snapshot.getValue(Boolean.class);
                        if (isTyping != null && isTyping) {
                            tvTypingStatus.setVisibility(View.VISIBLE);
                        } else {
                            tvTypingStatus.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Message msg = new Message(sender, receiver, message, System.currentTimeMillis());
        reference.child("Chats").push().setValue(msg);
    }

    private void readMessages(final String myid, final String userid) {
        mChat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message chat = snapshot.getValue(Message.class);
                    if (chat != null && chat.getReceiverId() != null && chat.getSenderId() != null) {
                        if (chat.getReceiverId().equals(myid) && chat.getSenderId().equals(userid) ||
                                chat.getReceiverId().equals(userid) && chat.getSenderId().equals(myid)) {
                            mChat.add(chat);
                        }
                    }
                }
                chatAdapter = new ChatAdapter(ChatActivity.this, mChat);
                recyclerChat.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        setTypingStatus(false);
    }
}