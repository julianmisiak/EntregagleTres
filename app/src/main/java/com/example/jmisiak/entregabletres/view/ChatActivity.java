package com.example.jmisiak.entregabletres.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jmisiak.entregabletres.R;
import com.example.jmisiak.entregabletres.model.ChatMessage;
import com.example.jmisiak.entregabletres.view.adapter.ChatViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.abdularis.civ.CircleImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> adapter;
    private DatabaseReference mReference;
    private FirebaseUser currentUser;
    public static final String PATH_CHAT_ARTWORK = "chatArtwork";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        displayChatMessages();
        mReference = FirebaseDatabase.getInstance().getReference();
        CircleImageView imgSession = findViewById(R.id.imgSession);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Glide.with(getApplicationContext()).load(currentUser.getPhotoUrl()).into(imgSession);
        }


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference id = mReference.child(PATH_CHAT_ARTWORK).push();
                EditText input = findViewById(R.id.input);
                id.setValue(new ChatMessage(id.getKey(), input.getText().toString(),
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName())
                );

                input.setText("");
            }
        });
    }

    private void displayChatMessages() {
        final RecyclerView recyclerView = findViewById(R.id.list_of_messages);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(ChatMessage.class, R.layout.message_cardview, ChatViewHolder.class, FirebaseDatabase.getInstance().getReference().child("chatArtwork")) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, ChatMessage chatMessage, int position) {
                viewHolder.setMessageText(chatMessage.getMessageText());
                viewHolder.setMessageUser(String.valueOf(chatMessage.getMessageUser()));
                viewHolder.setMessageTime((DateFormat.format("dd-MM-yyyy (HH:mm)", chatMessage.getMessageTime())).toString());
            }
        };


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                linearLayoutManager.smoothScrollToPosition(recyclerView, null, adapter.getItemCount());
            }
        });

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                final ChatMessage item = adapter.getItem(pos);

                mReference.child(PATH_CHAT_ARTWORK).child(item.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!item.getMessageUser().equals(currentUser.getDisplayName())){
                            adapter.notifyDataSetChanged();
                            Toast.makeText(ChatActivity.this, getResources().getString(R.string.only_remove_msj), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dataSnapshot.getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

    }


}
