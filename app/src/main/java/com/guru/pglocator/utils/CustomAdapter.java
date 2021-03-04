package com.guru.pglocator.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guru.pglocator.R;
import com.guru.pglocator.models.PgDetails;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<PgDetails> listValues;
    private  int layout;


    public CustomAdapter(Context context,int layout,List<PgDetails> listValues) {
        this.context=context;
        this.listValues=listValues;
        this.layout=layout;

    }

    @Override
    public int getCount() {
        return listValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(layout,null);
        TextView tvname=view.findViewById(R.id.tvhostelname);
        PgDetails pgDetails=listValues.get(position);
        tvname.setText(pgDetails.getHostelname());

        return view;
    }
}
