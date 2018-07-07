package com.example.mehdidjo.myapplication2;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mehdidjo.myapplication2.model.Author;
import com.example.mehdidjo.myapplication2.model.Message;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by Mehdi Djo on 19/04/2018.
 */


public class UserAdapter extends ArrayAdapter<Author>{

    Context mycontext;
    public UserAdapter(@NonNull Context context, ArrayList<Author> resource) {
        super(context, 0 , resource);
        mycontext= context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.users_list_item, parent, false);
        }


        Author author = getItem(position);
        ImageView imageView =(ImageView) listItemView.findViewById(R.id.userImage);
        TextView textView = (TextView) listItemView.findViewById(R.id.userName);
        TextView textView2 = (TextView) listItemView.findViewById(R.id.post);
        CheckBox checkBox =(CheckBox) listItemView.findViewById(R.id.chek);


        textView.setText(author.getName());
        if(author.getPoste() != null){
        textView2.setText(author.getPoste());
        } else {
            textView2.setText("inconnu");
        }


        String IMAGE_URL="https://wcc723.gitbooks.io/google_design_translate/content/images/usability/usability_bidirectionality_guidelines_when12.png";
        String uris = author.getAvatar();


        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(uris)
                .placeholder(R.drawable.icon_profile_empty)
                .transform(transformation)
                .into(imageView);

        return listItemView;
    }
}