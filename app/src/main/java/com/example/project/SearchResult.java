package com.example.project;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchResult extends AppCompatActivity {

    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        tvResult = findViewById(R.id.tvSearchResult);
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())){
            handleSearch(getIntent().getStringExtra(SearchManager.QUERY));
        }
    }

    private void handleSearch(String searchQuery) {
        tvResult.setText(searchQuery);
    }
}
