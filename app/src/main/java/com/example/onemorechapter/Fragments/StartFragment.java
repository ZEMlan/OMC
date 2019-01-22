package com.example.onemorechapter.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.onemorechapter.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class StartFragment extends androidx.fragment.app.Fragment {

    private int textState = 0;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_start, container, false);
        final TextView textView = layout.findViewById(R.id.textView);
        ImageButton imageButton = layout.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textState == 0){
                    textView.setVisibility(View.VISIBLE);
                    textState = 1;
                }
                else{
                    textView.setVisibility(View.INVISIBLE);
                    textState = 0;
                }
            }
        });
        return layout;
    }

}
