package com.example.mehdidjo.myapplication2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Dialog;
import com.example.mehdidjo.myapplication2.model.Document;
import com.example.mehdidjo.myapplication2.model.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Mehdi Djo on 10/03/2018.
 */

public class DocFragment extends Fragment  {

    private static int RC_FILE_PICKER=2;
    private static int CAMERA_REQUEST=3;

    private RecyclerView recyclerView;
    private DocAdapter adapter;
    private List<Document> docList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ChildEventListener mchildEventListener;
    SwipeRefreshLayout mSwipeRefreshLayout;

    String path = "https://archives.uqam.ca/upload/files/guide-redaction-proces-verbaux.pdf";
    String path2 = "https://www.univ-montp3.fr/infocom/wp-content/REC-compte-rendu-2017.pdf";

public  DocFragment (){

}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.document_activity, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Docs");


        docList = new ArrayList<>();
        adapter = new DocAdapter(getContext(), docList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        long Time = System.currentTimeMillis() ;
        Date dateobject = new Date(Time);
        String formattedDate = formatDate(dateobject);
        docList.add(new Document("id" ,"Facture",path,formattedDate ,0 , "Mouloud sayhi"));
        docList.add(new Document("id" ,"Fiche technique","https://www.d3e.fr/pdf/geoxhv6.pdf",formattedDate ,1 , "Rafik mebarki"));
        docList.add(new Document("id" ,"Devis ","http://www.brignoles.fr/documents/Documents/Vie_pratique/Urbanisme/Renouvellement_urbain/exemple_DEVIS_FACTURE_2012.pdf" ,formattedDate,2));
        docList.add(new Document("id" ,"Procés-verbal",path ,formattedDate,3 , "Riad Azizi"));
        docList.add(new Document("id" ,"compte-rendu",path2 ,formattedDate,0 , "Kamel Boulefaa"));
        docList.add(new Document("id" ,"Fiche technique","https://www.d3e.fr/pdf/geoxhv6.pdf",formattedDate ,1));
        docList.add(new Document("id" ,"Procés-verbal",path ,formattedDate,3));
        adapter.notifyDataSetChanged();


        attachDatabaseReadListener();
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottemSheetDoc bottomSheet = new BottemSheetDoc();
                bottomSheet.show(getActivity().getSupportFragmentManager() ,"exampleBottomSheet");
            }
        });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
         //.getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                 adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                 adapter.getFilter().filter(query);
                return false;
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                docList.clear();
                mchildEventListener = null;
                attachDatabaseReadListener();
                createDocs();
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }


    private void attachDatabaseReadListener() {


        if (mchildEventListener == null){
            mchildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Document doc = dataSnapshot.getValue(Document.class);
                    docList.add(doc);
                    adapter.notifyDataSetChanged();

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

    private String formatTime(Date dateobject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateobject);
    }

    private String formatDate(Date dateobject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        return dateFormat.format(dateobject);
    }

    private void createDocs(){

        long Time = System.currentTimeMillis() ;
        Date dateobject = new Date(Time);
        String formattedDate = formatDate(dateobject);
        docList.add(new Document("id" ,"Facture",path,formattedDate ,0 , "Mouloud sayhi"));
        docList.add(new Document("id" ,"Fiche technique","https://www.d3e.fr/pdf/geoxhv6.pdf",formattedDate ,1 , "Rafik mebarki"));
        docList.add(new Document("id" ,"Devis ","http://www.brignoles.fr/documents/Documents/Vie_pratique/Urbanisme/Renouvellement_urbain/exemple_DEVIS_FACTURE_2012.pdf" ,formattedDate,2));
        docList.add(new Document("id" ,"Procés-verbal",path ,formattedDate,3 , "Riad Azizi"));
        docList.add(new Document("id" ,"compte-rendu",path2 ,formattedDate,0 , "Kamel Boulefaa"));
        docList.add(new Document("id" ,"Fiche technique","https://www.d3e.fr/pdf/geoxhv6.pdf",formattedDate ,1));
        docList.add(new Document("id" ,"Procés-verbal",path ,formattedDate,3));
        adapter.notifyDataSetChanged();
    }

}
