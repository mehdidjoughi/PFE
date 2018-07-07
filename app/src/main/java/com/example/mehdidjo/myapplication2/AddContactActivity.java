package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mehdidjo.myapplication2.model.Author;
import com.example.mehdidjo.myapplication2.model.Dialog;
import com.example.mehdidjo.myapplication2.model.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {

    String IMAGE_URL="https://wcc723.gitbooks.io/google_design_translate/content/images/usability/usability_bidirectionality_guidelines_when12.png";
    android.support.v7.widget.Toolbar toolbar;
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;
    private DatabaseReference myRefDialoge;
    private ChildEventListener mchildEventListener;
    private ArrayList<Author> userlist;
    EditText editText;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Créer un groupe");



        userlist= new ArrayList<>();
        ListView listView = findViewById(R.id.userList);

        editText = findViewById(R.id.group_name);


        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference().child("Users");
        attachDatabaseReadListener();


//        button = findViewById(R.id.btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                myRefDialoge = database.getReference().child("Dialoge");
//                String id=  myRefDialoge.push().getKey();
//                Dialog dialog= new Dialog(id, editText.getText().toString(),IMAGE_URL,userlist,null,0);
//                myRefDialoge.child(id).setValue(dialog);
//                finish();
//            }
//        });

        UserAdapter userAdapter = new UserAdapter(this , userlist);
        listView.setAdapter(userAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.check) {

            if ( !editText.getText().toString().isEmpty()) {

                myRefDialoge = database.getReference().child("Dialoge");
                String ide = myRefDialoge.push().getKey();
                Dialog dialog = new Dialog(ide, editText.getText().toString(), IMAGE_URL, userlist, null, 0);
                myRefDialoge.child(ide).setValue(dialog);
                finish();
            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void attachDatabaseReadListener() {

        if (mchildEventListener == null){
            mchildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Author author = dataSnapshot.getValue(Author.class);
                    userlist.add(author);

                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            myRefUsers.addChildEventListener(mchildEventListener);
        }
    }
}
