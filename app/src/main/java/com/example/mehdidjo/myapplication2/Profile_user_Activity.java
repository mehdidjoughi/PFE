package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mehdidjo.myapplication2.model.Author;
import com.example.mehdidjo.myapplication2.model.Dialog;
import com.example.mehdidjo.myapplication2.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class Profile_user_Activity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    private FirebaseDatabase database;
    private DatabaseReference myRefDialoge;
    String IMAGE_URL="https://wcc723.gitbooks.io/google_design_translate/content/images/usability/usability_bidirectionality_guidelines_when12.png";
    private ArrayList<Author> userlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user_);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database = FirebaseDatabase.getInstance();
        myRefDialoge = database.getReference().child("Dialoge");
        Bundle bundle = getIntent().getExtras();
       final String profileName = bundle.getString("name");
        getSupportActionBar().setTitle(profileName);

        final String profileEmail = bundle.getString("email");
       final String profilePhone = bundle.getString("phone");
        final String profileAvater = bundle.getString("avatar");
        String profilePoste = bundle.getString("poste");
        String profileId = bundle.getString("id");

        ImageView imageView = findViewById(R.id.profile_pic);
        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        TextView poste = findViewById(R.id.poste);


        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getApplicationContext())
                .load(profileAvater)
                .placeholder(R.drawable.icon_profile_empty)
                .transform(transformation)
                .into(imageView);

        name.setText(profileName);
        phone.setText(profilePhone);
        email.setText(profileEmail);
        poste.setText(profilePoste);

        LinearLayout callLayout = findViewById(R.id.callAction);
        LinearLayout messageLayout = findViewById(R.id.messageAction);
        LinearLayout emailLayout = findViewById(R.id.mailAction);

        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + profilePhone));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                }
                startActivity(intent);
            }
        });

        userlist= new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userlist.add(new Author(user.getUid() , user.getDisplayName() , user.getPhotoUrl().toString()));
        userlist.add(new Author(profileId ,profileName ,profileAvater));
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Author author = new Author("sqdqd" , "mehdi");
                Message message = new Message(" " , "abcd" , author);
                myRefDialoge = database.getReference().child("Dialoge");
                String id=  myRefDialoge.push().getKey();
                Dialog dialog= new Dialog(id, profileName,profileAvater,userlist,null,0);
                myRefDialoge.child(id).setValue(dialog);

                toDialog(profileName , dialog);
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                String[] to = {profileEmail};
                intent.putExtra(Intent.EXTRA_EMAIL, to);
                intent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(intent, "Send email");
                startActivity(chooser);
            }
        });

    }

    private void toDialog(String name , Dialog dialog){
        Intent intent = new Intent(getApplicationContext() ,ChatActivity.class );
        intent.putExtra("DialogName" , name);
        intent.putExtra("Dialge" ,  dialog.getId());
        startActivity(intent);
    }
}
