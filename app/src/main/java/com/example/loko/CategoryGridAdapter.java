package com.example.loko;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class CategoryGridAdapter extends BaseAdapter {
    private List<String> catList;

    public CategoryGridAdapter(List<String> catList) {
        this.catList = catList;
    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if(convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        }
        else
        {
            view= convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(parent.getContext(),SetsActivity.class);
                intent.putExtra("CAT",catList.get(position));
                intent.putExtra("CATEGORY_ID",position+1);
                parent.getContext().startActivity(intent);
            }
        });


        ((TextView) view.findViewById(R.id.categoryName)).setText(catList.get(position));
        Random rmd=new Random();
        int color= Color.argb(255,rmd.nextInt(255),rmd.nextInt(255),rmd.nextInt(255));
        view.setBackgroundColor(color);
        return view;
    }
}
