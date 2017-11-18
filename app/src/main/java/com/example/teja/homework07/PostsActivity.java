package com.example.teja.homework07;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PostsActivity extends AppCompatActivity {
    private DatabaseReference dataref, datarefUser, dataRefNew;
    private FirebaseAuth mAuth;
    RecyclerView chatRecyclerView;
    PostsAdapter adapter;
    static private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Message> messageArrayList = new ArrayList<Message>();
    EditText messageData;
    public static User userData = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        messageData = (EditText) findViewById(R.id.sendmessage);
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatroom);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        TextView userName = (TextView) findViewById(R.id.threadname);
        userName.setText(currentUser.getDisplayName());
        getMessageDetails();
        if (messageArrayList.size() > 0){
            mLayoutManager = new LinearLayoutManager(PostsActivity.this);
            chatRecyclerView.setLayoutManager(new LinearLayoutManager(PostsActivity.this, LinearLayoutManager.VERTICAL, false));
            adapter = new PostsAdapter(messageArrayList, PostsActivity.this);
            chatRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        //Retrieve data
        dataref = FirebaseDatabase.getInstance().getReference().child("Message");
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = messageData.getText().toString().trim();
                if (!text.equals("")) {
                    final Message message = new Message();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    message.setMessage(text);
                    message.setUserId(currentUser.getUid().toString());
                    datarefUser = FirebaseDatabase.getInstance().getReference().child("Users");
                    datarefUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                User userdata = postSnapshot.getValue(User.class);
                                Log.d("The ui1", "is" + userdata.getUserId());
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (userdata.getUserId().equals(currentFirebaseUser.getUid().toString())) {
                                    userData = userdata;
                                    Log.d("The userdata","name"+userData.getFirstName());
                                    message.setName(userData.getFirstName());
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
                                    String date = df.format(Calendar.getInstance().getTime());
                                    message.setTime(date);
                                    dataref = FirebaseDatabase.getInstance().getReference().child("Message");
                                    dataref.push().setValue(message);
                                    getMessageDetails();
                                    messageData.setText("");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Add data
                }else {
                    Toast.makeText(PostsActivity.this, "Please enter valid message",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(PostsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }
    public void getMessageDetails(){
        final ArrayList<Message> messages = new ArrayList<Message>();
        dataRefNew = FirebaseDatabase.getInstance().getReference().child("Message");
        dataRefNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Message msg = postSnapshot.getValue(Message.class);
                    Log.d("The ui2","is"+msg.getUserId());
                    //FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    messages.add(msg);
                    if (messages.size()>0) {
                        mLayoutManager = new LinearLayoutManager(PostsActivity.this);
                        chatRecyclerView.setLayoutManager(new LinearLayoutManager(PostsActivity.this, LinearLayoutManager.VERTICAL, false));
                        adapter = new PostsAdapter(messages, PostsActivity.this);
                        chatRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }
}
