package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by Mehdi Djo on 10/03/2018.
 */

public class ProfileFragment extends Fragment {

    public ProfileFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_activity, container, false);

      ImageView  profilePic = (ImageView)rootView.findViewById(R.id.profile_image);
      TextView nameView = (TextView) rootView.findViewById(R.id.id);
      TextView emailView = (TextView) rootView.findViewById(R.id.info);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String name = user.getDisplayName();
        String email = user.getEmail();

        nameView.setText(name);
        emailView.setText(email);

        Uri uri = user.getPhotoUrl();

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getActivity())
                .load(uri)
                .placeholder(R.drawable.icon_profile_empty)
                .transform(transformation)
                .into(profilePic);

        // déconecter
        Button logOutButton = rootView.findViewById(R.id.logout_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getContext());
                Intent intent = new Intent(getContext() , Auth_Activity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
