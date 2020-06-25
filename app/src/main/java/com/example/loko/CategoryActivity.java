package com.example.loko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static com.example.loko.SplashActivity.catList;

public class CategoryActivity extends AppCompatActivity {
private GridView CategoryGrid;
private Toolbar toolbar;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CATEGORIES");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        CategoryGrid= findViewById(R.id.CategoryGrid);
        CategoryGridAdapter adapter=new CategoryGridAdapter(catList);
        CategoryGrid.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            CategoryActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}