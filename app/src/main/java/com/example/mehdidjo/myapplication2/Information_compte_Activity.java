package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Author;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class Information_compte_Activity extends AppCompatActivity {

    private EditText phoneEdit , postEdit;
    private Button doneButton;
    private String phone ,post;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView helloView;
    private ImageView imageView;
    private ChildEventListener mchildEventListener;
    private String curentuId;
    String uphone=null;
    String uposte=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_compte_);

        database = FirebaseDatabase.getInstance();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         curentuId = user.getUid();
        myRef = database.getReference().child("Users");

        attachDatabaseReadListener();
        phoneEdit = findViewById(R.id.phone);
        postEdit = findViewById(R.id.post);
        doneButton = findViewById(R.id.done);
        helloView = findViewById(R.id.textmessage);
        imageView = findViewById(R.id.profile_image2);


        Uri uri = user.getPhotoUrl();
        String userName = user.getDisplayName();
        helloView.setText("BIENVENUE "+userName.toUpperCase());

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getApplication())
                .load(uri)
                .placeholder(R.drawable.icon_profile_empty)
                .transform(transformation)
                .into(imageView);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uphone != null ){
                    phoneEdit.setText(uphone);
                }
                if (uposte != null){
                    postEdit.setText(uposte);
                }
                phone = phoneEdit.getText().toString().trim();
                post = postEdit.getText().toString().trim();

                if (phone.isEmpty()){
                    Toast.makeText(Information_compte_Activity.this , "Please inter you phone" , Toast.LENGTH_SHORT).show();
                }else if (post.isEmpty()){
                    Toast.makeText(Information_compte_Activity.this , "Please inter you poste" , Toast.LENGTH_SHORT).show();
                }else {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();
                    String id = user.getUid();
                    Author auther = new Author(id ,name , email ,photoUrl.toString() , phone , post);
                    myRef.child(id).setValue(auther);

                    Intent intent = new Intent(Information_compte_Activity.this , PagerTable.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void attachDatabaseReadListener() {

        if (mchildEventListener == null) {
            mchildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Author author = dataSnapshot.getValue(Author.class);

                    Log.v("Information activity" , "----------> user  : "+author.getName()+"\nid : "+author.getId()+"curent uid : "+curentuId+" \nphone :" +author.getPhone()+"\navatar : "+author.getAvatar()
                            +"\nEmail : "+author.getEmail()+"\nposte : "+author.getPoste());

                   if (curentuId == author.getId() ) {
                        uphone = author.getPhone();
                        uposte = author.getPoste();
                   }
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
            myRef.addChildEventListener(mchildEventListener);
        }
    }

}
