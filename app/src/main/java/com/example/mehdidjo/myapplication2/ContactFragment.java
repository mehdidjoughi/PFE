package com.example.mehdidjo.myapplication2;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Author;
import com.example.mehdidjo.myapplication2.model.Dialog;
import com.example.mehdidjo.myapplication2.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import java.util.ArrayList;


/**
 * Created by Mehdi Djo on 10/03/2018.
 */

public class ContactFragment extends Fragment {

    public ContactFragment (){

    }

    String IMAGE_URL="https://firebasestorage.googleapis.com/v0/b/friendlychat-b3fb5.appspot.com/o/groupes.png?alt=media&token=8cbe9186-8e23-49b1-86c2-87863e61ac6e";
    private CoordinatorLayout coordinatorLayout;
    private DialogsListAdapter<Dialog> adapter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Dialog> chats;
    private ArrayList<Dialog> dialogs;
    private ChildEventListener mchildEventListener;
    private FloatingActionButton fab;
    private Author curentAuthor;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DialogsList dialogsList;
    private ArrayList<Author> usersdemo;

   ImageLoader imageLoader = new ImageLoader() {
        @Override
        public void loadImage(ImageView imageView, String url) {
            Picasso.with(getContext()).load(url).into(imageView);
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dialoge_liste, container, false);

        dialogsList =rootView.findViewById(R.id.dialogsList);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setRefreshing(true);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Dialoge");
        FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(curentUser.getPhotoUrl() != null) {
            curentAuthor = new Author(curentUser.getUid(), curentUser.getDisplayName(), curentUser.getEmail(), curentUser.getPhotoUrl().toString());
        }else {
            curentAuthor = new Author(curentUser.getUid(), curentUser.getDisplayName(), curentUser.getEmail());
        }

        dialogs = new ArrayList<>();
        adapter = new DialogsListAdapter<Dialog>(imageLoader);

        ConnectivityManager connMang = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMang.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){

            createDefaultGroupes();
            attachDatabaseReadListener(0);
            mSwipeRefreshLayout.setRefreshing(false);
        }else {

            createDefaultGroupes();
            Toast.makeText(getActivity() , "No Internet Connexion" , Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }







        // swipe refresh
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                mchildEventListener =null;
                createDefaultGroupes();
                attachDatabaseReadListener(1);
                adapter.addItems(dialogs);
                dialogs.clear();

                dialogsList.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });




        // item click
        adapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {

                String name = dialog.getDialogName();
                toDialog(name , dialog);
            }
        });

        // long click
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        adapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<Dialog>() {
            @Override
            public void onDialogLongClick(final Dialog dialog) {

                Snackbar snackbar = Snackbar.make(coordinatorLayout , "Supprimer le groupe ", Snackbar.LENGTH_LONG)
                        .setAction("Supprimer", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter.deleteById( dialog.getId());
                                //myRef.removeValue().
                            }
                        });
                snackbar.show();
            }
        });

        dialogsList.setAdapter(adapter);
        return  rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        mchildEventListener =null;
        createDefaultGroupes();
        attachDatabaseReadListener(1);


        adapter.addItems(dialogs);
        dialogsList.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);

    }
    @Override
    public void onPause() {
        super.onPause();
        adapter.clear();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter.clear();

    }

    private void toDialog(String name , Dialog dialog){
        Intent intent = new Intent(getContext() ,ChatActivity.class );
        intent.putExtra("DialogName" , name);
        intent.putExtra("Dialge" ,  dialog.getId());
        startActivity(intent);
    }

    private void attachDatabaseReadListener(final int i) {


        if (mchildEventListener == null){
            mchildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Dialog dialog = dataSnapshot.getValue(Dialog.class);

                    if(dialog.getMyLastMessage() ==null) {
                        Message message = new Message("hello", "abcd", curentAuthor);
                        dialog.setMyLastMessage(message);
                    }
                    if(i==0){
                        // starting
                        adapter.addItem( dialog);
                    }else {
                        // swipe refresh
                        dialogs.add(dialog);
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

    private void createDefaultGroupes(){
        usersdemo= new ArrayList<>();
        Message message = new Message("hello", "abcd", curentAuthor);
        adapter.addItem( new Dialog("qsjknd" , "Administration" ,IMAGE_URL,usersdemo,message,2));
        adapter.addItem( new Dialog("qsjknd" , "Gestion" ,IMAGE_URL,usersdemo,message,1));
        Message message1 = new Message("voici le raport", "abcd", curentAuthor);
        adapter.addItem( new Dialog("qsjknd" , "Marketing" ,IMAGE_URL,usersdemo,message1,0));
        Message message0 = new Message("d'acord", "abcd", curentAuthor);
        adapter.addItem( new Dialog("qsjknd" , "Evenement" ,IMAGE_URL,usersdemo,message0,0));
    }
}
