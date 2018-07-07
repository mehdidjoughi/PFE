package com.example.mehdidjo.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Author;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class All_User_Activity extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    android.support.v7.widget.Toolbar toolbar;
    FirebaseListAdapter<Author> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__user_);

        //Previous versions of Firebase
        Firebase.setAndroidContext(this);

        //Newer version of Firebase
        if (!FirebaseApp.getApps(this).isEmpty()) {
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Contacts");


        ListView listView = findViewById(R.id.allUserList);
        View progressBar = findViewById(R.id.progress);

        ConnectivityManager connMang = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMang.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){


            TextView textView = findViewById(R.id.empty);
            textView.setText("Chargement");
            listView.setEmptyView(textView);
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("Users");


            Firebase mRef = new Firebase("https://testchat-1931f.firebaseio.com/Users/");
            myAdapter = new FirebaseListAdapter<Author>(this,
                    Author.class,
                    R.layout.all_users_list_item,
                    mRef
            ) {
                @Override
                protected void populateView(View view, final Author author, int i) {

                    Log.v("Al user Avtivity", "-------> PopulateView done");
                    ImageView imageView = (ImageView) view.findViewById(R.id.userImage);
                    LinearLayout item = (LinearLayout) view.findViewById(R.id.item);
                    TextView textView = (TextView) view.findViewById(R.id.userName);
                    TextView textView2 = (TextView) view.findViewById(R.id.post);
                    ImageView phoneAction = (ImageView) view.findViewById(R.id.phoneAction);
                    textView.setText(author.getName());
                    if (author.getPoste() != null) {
                        textView2.setText(author.getPoste());
                    } else {
                        textView2.setText("inconnu");
                    }
                    String uris = author.getAvatar();
                    Transformation transformation = new RoundedTransformationBuilder()
                            .cornerRadiusDp(30)
                            .oval(false)
                            .build();
                    Picasso.with(getApplicationContext())
                            .load(uris)
                            .placeholder(R.drawable.icon_profile_empty)
                            .transform(transformation)
                            .into(imageView);
                    final String phone = author.getPhone();
                    phoneAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            call(phone);
                        }
                    });

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(All_User_Activity.this, Profile_user_Activity.class);
                            i.putExtra("name" , author.getName());
                            i.putExtra("id" ,    author.getId());
                            i.putExtra("avatar" , author.getAvatar());
                            i.putExtra("email" , author.getEmail());
                            i.putExtra("phone" , author.getPhone());
                            i.putExtra("poste" , author.getPoste());
                            startActivity(i);
                        }
                    });
                }


            };

            progressBar.setVisibility(View.GONE);

        }else {

            progressBar.setVisibility(View.GONE);
            TextView textView = findViewById(R.id.empty);
            textView.setText("No Internet Connexion");
            listView.setEmptyView(textView);

        }


        listView.setAdapter(myAdapter);


    }


    private void call(String s) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

        }
       startActivity(intent);
    }

}
