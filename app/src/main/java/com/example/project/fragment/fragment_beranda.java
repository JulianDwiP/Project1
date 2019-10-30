package com.example.project.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.BukuAdapter;
import com.example.project.R;
import com.example.project.sqlHelper.WorldListOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_beranda extends Fragment {


    public fragment_beranda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        WorldListOpenHelper mDB = new WorldListOpenHelper(c);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        BukuAdapter bukuAdapter = new BukuAdapter(c, mDB);
        recyclerView.setAdapter(bukuAdapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

}
