package com.example.mehdidjo.myapplication2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Document;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mehdi Djo on 17/05/2018.
 */

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.MyViewHolder> implements Filterable{

    private Context mContext;
    private List<Document> docList;
    private List<Document> contactList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseStorage mFirebaseStorage;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date,owner;
        public ImageView thumbnail, overflow;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
            thumbnail = (ImageView) view.findViewById(R.id.thumb);
            overflow = (ImageView) view.findViewById(R.id.action);
            owner = view.findViewById(R.id.owner);

        }
    }


    public DocAdapter(Context mContext, List<Document> albumList) {
        this.mContext = mContext;
        this.docList = albumList;
        this.contactList = albumList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
         Document document = docList.get(position);
         holder.title.setText(document.getName());
         holder.date.setText("Modifié : "+document.getDate());
         if ( ! TextUtils.isEmpty(document.getAuthor())) {
             holder.owner.setText("crée par : " + document.getAuthor());
         }else{holder.owner.setText("crée par : Admin");}

         int type = document.getType();
         switch (type){
             case 0:  holder.thumbnail.setImageResource(R.drawable.ic_baseline_folder_24px); break;
             case 1:  holder.thumbnail.setImageResource(R.drawable.ic_insert_drive_file_black_24dp); break;
             case 2:  holder.thumbnail.setImageResource(R.drawable.ic_picture_as_pdf_black_24dp); break;
             case 3:  holder.thumbnail.setImageResource(R.drawable.ic_image_black_24dp); break;
             default: holder.thumbnail.setImageResource(R.drawable.ic_baseline_folder_24px); break;
         }


         final Document doc = document;

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow , doc);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view ,Document document) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_doc, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(document));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        Document doc;
        public MyMenuItemClickListener(Document doc) {
            this.doc = doc;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.Download:

                    mFirebaseStorage = FirebaseStorage.getInstance();
                    mChatPhotosStorageReference = mFirebaseStorage.getReference().child("Docs_strorage");
                    StorageReference ref = mChatPhotosStorageReference.child(doc.getName());

                    String url = doc.getPath();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);


/*
                    try {
                        File rootPath = new File(Environment.getExternalStorageDirectory(), "YaCollab_File");
                        if(!rootPath.exists()) {
                            rootPath.mkdirs();
                        }

                        final File localFile = new File(rootPath,"localfile.pdf");

                      // final  CoordinatorLayout coordinatorLayout = mContext.getApplicationContext().
                        File file = File.createTempFile("tst" ,"pdf");
                        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

//                                Snackbar snackbar = Snackbar.make(coordinatorLayout , "Delete this dialog", Snackbar.LENGTH_LONG);
//                                snackbar.show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {

                                Toast.makeText(mContext, "onComplete", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "onFailure", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

*/

                    notifyDataSetChanged();
                    return true;


                case R.id.Remove:
                    Toast.makeText(mContext, "Remove", Toast.LENGTH_SHORT).show();
                    docList.remove(doc);
                    notifyDataSetChanged();



                    return true;


                case R.id.Rename:

                    Toast.makeText(mContext, "Rename", Toast.LENGTH_SHORT).show();

                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference().child("Docs");

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.add_folder_dialog);
                    dialog.setTitle("Renommer le fichier");
                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    final EditText editText = (EditText) dialog.findViewById(R.id.editDossier);
                    editText.setText(doc.getName());
                    editText.selectAll();

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            long Time = System.currentTimeMillis() ;
                            Date dateobject = new Date(Time);
                            String formattedDate = formatDate(dateobject);
                            myRef.child(doc.getId()).setValue(new Document(doc.getId() ,editText.getText().toString(),doc.getPath(),formattedDate ,doc.getType()));
                            notifyDataSetChanged();
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

                    return true;
                default:
            }
            return false;
        }
        private String formatDate(Date dateobject) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
            return dateFormat.format(dateobject);
        }
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    docList = contactList;
                } else {
                    List<Document> filteredList = new ArrayList<>();
                    for (Document row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    docList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = docList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                docList = (ArrayList<Document>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 }
