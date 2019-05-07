package com.example.onemorechapter.info;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.onemorechapter.R;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


public class InfoFragment extends Fragment {


    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button thanks = view.findViewById(R.id.buttonThanks);
        thanks.setOnClickListener(v ->
                Snackbar.make(v, "Вам спасибо за спасибо!", Snackbar.LENGTH_SHORT).show());
    }
}
