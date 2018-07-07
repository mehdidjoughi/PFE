package com.example.mehdidjo.myapplication2;


import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Document;
import com.example.mehdidjo.myapplication2.model.Message;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class PagerTable extends AppCompatActivity implements BottemSheetDoc.BottomSheetListner2{

    android.support.v7.widget.Toolbar toolbar;
    private static int RC_FILE_PICKER=2;
    private static int CAMERA_REQUEST=3;
    private static final int RC_SIGN_IN = 123;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private  FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_table);



                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null){
                    // déconncter

                    Intent intent = new Intent(PagerTable.this , Auth_Activity.class);
                    startActivity(intent);
                }else {

                    toolbar = findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);
                    CategoryAdapter adapter = new CategoryAdapter(getApplicationContext(), getSupportFragmentManager());
                    viewPager.setAdapter(adapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);

                }






    }
/* ------------------------------------------------------------------------------------------------------------*/


    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addContact) {

            Intent intent = new Intent(getApplicationContext() , AddContactActivity.class);
            startActivity(intent);
            return true;

        } if (id == R.id.usersList){

            Intent intent = new Intent(getApplicationContext() , All_User_Activity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClicked2(int id) {

        switch (id){

            case  R.id.dossier :


                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_folder_dialog);
                dialog.setTitle("Nouveau dossier");
                Button ok = (Button) dialog.findViewById(R.id.ok);

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("Docs");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText = (EditText) dialog.findViewById(R.id.editDossier);
                        Toast.makeText(getApplicationContext() , "Le dossier "+editText.getText().toString()+" est crée" ,Toast.LENGTH_SHORT).show();

                        String id = myRef.push().getKey();


                        long Time = System.currentTimeMillis() ;
                        Date dateobject = new Date(Time);
                        String formattedDate = formatDate(dateobject);
                        String formatedTime = formatTime(dateobject);
                        myRef.child(id).setValue(new Document(id ,editText.getText().toString(),"/folder",formattedDate ,0));
                        dialog.dismiss();
                    }
                });

                Button annuler = (Button) dialog.findViewById(R.id.annuler);
                annuler.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;


            case  R.id.importer :


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_FILE_PICKER);
                break;


            case  R.id.numr :



                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Toast.makeText(getApplicationContext() , " Successfully signed in" , Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(PagerTable.this , Information_compte_Activity.class);
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED){
                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext() , "Sign in failed" , Toast.LENGTH_SHORT).show();

            }
        }

        if (requestCode == RC_FILE_PICKER ) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();


                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("Docs");

                long Time = System.currentTimeMillis() ;
                Date dateobject = new Date(Time);
               final String formattedDate = formatDate(dateobject);
                // Get a reference to store file at chat_photos/<FILENAME>
                mFirebaseStorage = FirebaseStorage.getInstance();
                mChatPhotosStorageReference = mFirebaseStorage.getReference().child("Docs_strorage");
                StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
               final String name = selectedImageUri.getLastPathSegment().toString();


                // Upload file to Firebase Storage
                photoRef.putFile(selectedImageUri)
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // When the image has successfully uploaded, we get its download URL
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                // Set the download URL to the message box, so that the user can send it to the database

                                String id = myRef.push().getKey();
                                myRef.child(id).setValue(new Document(id , name,downloadUrl.toString(),formattedDate ,1));
                                Toast.makeText(getApplicationContext(), "le " + name+" est impotré", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "erreur d'importation", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (requestCode == CAMERA_REQUEST){
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();

                  Bitmap photo = (Bitmap) data.getExtras().get("data");
               // Bitmap photo = (Bitmap) data.getData();

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                Toast.makeText(getApplicationContext() , "CAMERA_REQUEST "+getPath(tempUri) , Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String formatTime(Date dateobject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateobject);
    }

    private String formatDate(Date dateobject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        return dateFormat.format(dateobject);
    }

}
