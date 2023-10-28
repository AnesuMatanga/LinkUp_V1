package com.example.linkupappv1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    //Initialize
    FirebaseFirestore db;

    FirebaseAuth auth;
    FirebaseUser currentUser;

    EditText messageInputEditText;

    ImageView messageSendBtnImg;

    //Recycler View and Adapter to use to show messages in chat
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Initialize views and adapter using R.id
        messageInputEditText = findViewById(R.id.messageInputEditText);
        messageSendBtnImg = findViewById(R.id.messageInputSendBtn);
        recyclerView = findViewById(R.id.messageSentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messages);
        //Set the adapter
        recyclerView.setAdapter(messageAdapter);

        //Get currentUser
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        //Create db instance
        db = FirebaseFirestore.getInstance();

        //Get recipientUserId(User the message is being sent to) sent through intent as extra to create unique chatId for users
        String recipientUserId = getIntent().getStringExtra("recipientUserId");

        //Sorting the ids
        List<String> userIds = Arrays.asList(currentUser.getUid(), recipientUserId);
        Collections.sort(userIds);
        String uniqueChatId = userIds.get(0) + "_" + userIds.get(1);

        //Reference chat if its there, if not it will create one
        CollectionReference messagesReference = db.collection("Chats").document(uniqueChatId).collection("Messages");

        //Create onClickListener for sendButton

        messageSendBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create a new message document with msg data including timeStamp
                Map<String, Object> message = new HashMap<String, Object>();
                message.put("messageContent", messageInputEditText.getText().toString());
                message.put("senderId", currentUser.getUid().toString());
                message.put("timestamp", FieldValue.serverTimestamp());

                //Adding the message to Firestore
                messagesReference.add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Message has successfully been sent to cloud
                        //Toast message for debugging purposes
                        Toast.makeText(MessageActivity.this, "Message sent successfully!",
                                Toast.LENGTH_SHORT).show();
                        //Remove text from textBox
                        messageInputEditText.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Message was not sent!", e);
                        Toast.makeText(MessageActivity.this, "Message Not Sent!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //Listen to changes in the messages in Firestore (Order by timeStamp)
        messagesReference.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //If error
                if (error != null){
                    Log.w(TAG, "Something went wrong Listening!", error);
                    return;
                }
                //If there is a document change(new messages)
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if(documentChange.getType() == DocumentChange.Type.ADDED) {
                        //If new message has been added notify adapter for data change
                        //ToDo Update the UI here for new message
                        Message newMessage = documentChange.getDocument().toObject(Message.class);
                        //Add to the list of messages to be populated on the screen
                        messages.add(newMessage);

                    }
                }
                //Let the adapter know data has changed
                messageAdapter.notifyDataSetChanged();
            }
        });
    }
}