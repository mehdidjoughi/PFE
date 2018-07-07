package com.example.mehdidjo.myapplication2;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Mehdi Djo on 28/02/2018.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private  BottomSheetListner mListner;
    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);


        LinearLayout imagePicker = (LinearLayout) v.findViewById(R.id.imagePicker);
        LinearLayout photoPicker = (LinearLayout) v.findViewById(R.id.photoPicker);
        LinearLayout filePicker = (LinearLayout) v.findViewById(R.id.filePcker);

        int id=imagePicker.getId();

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListner.onButtonClicked(view.getId());


                dismiss();

            }
        });
        photoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListner.onButtonClicked(view.getId());
                dismiss();
            }
        });
        filePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListner.onButtonClicked(view.getId());
                dismiss();

            }
        });


        return v;
    }

    public void test(View view){
    }

    public interface BottomSheetListner {
        void onButtonClicked(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListner = (BottomSheetListner) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement BottomSheetListner ");
        }
    }
}
