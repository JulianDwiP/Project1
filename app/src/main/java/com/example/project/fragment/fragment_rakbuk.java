package com.example.project.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_rakbuk extends Fragment {


    public fragment_rakbuk() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_rakbuk, container, false);
        return view;
    }

}
