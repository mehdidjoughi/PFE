package com.example.mehdidjo.myapplication2;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Mehdi Djo on 12/05/2018.
 */

public class BottemSheetDoc extends BottomSheetDialogFragment {

    private BottomSheetListner2 mListner;
    public BottemSheetDoc() {
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
        View v= inflater.inflate(R.layout.bottom_sheet_dec, container, false);


        LinearLayout dossier = (LinearLayout) v.findViewById(R.id.dossier);
        LinearLayout importer = (LinearLayout) v.findViewById(R.id.importer);
        LinearLayout numr = (LinearLayout) v.findViewById(R.id.numr);

        int id=dossier.getId();

        dossier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListner.onButtonClicked2(view.getId());


                dismiss();

            }
        });
        importer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListner.onButtonClicked2(view.getId());
                dismiss();
            }
        });
        numr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListner.onButtonClicked2(view.getId());
                dismiss();

            }
        });


        return v;
    }

    public void test(View view){
    }

    public interface BottomSheetListner2 {
        void onButtonClicked2(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListner = (BottomSheetListner2) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement BottomSheetListner2 ");
        }
    }


}
