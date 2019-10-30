package com.example.project.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_kategori extends Fragment {

    public fragment_kategori() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kategori, container, false);
        String[] value = {"Pendidikan", "Informatika", "Sosial Budaya", "Fiksi", "Novel"};
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_item, value);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinnerAdapter);
        return view;
    }

}
