package com.guru.pglocator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;
import com.guru.pglocator.models.PgOwner;

import java.util.List;

class UsersAdapter  extends BaseAdapter {

    Context context;
    List<PgOwner> pgOwners;
    String usertype;
    public UsersAdapter(Context context, List<PgOwner> pgOwnerList,String usertype) {
        this.pgOwners=pgOwnerList;
        this.context=context;
        this.usertype=usertype;

    }

    @Override
    public int getCount() {
        return pgOwners.size();
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

        convertView= LayoutInflater.from(context).inflate(R.layout.row_users,null);
        TextView tvname=convertView.findViewById(R.id.tvname);
        TextView tvmobile=convertView.findViewById(R.id.tvmobile);
        TextView tvemail=convertView.findViewById(R.id.tvemail);
        TextView tvusertype=convertView.findViewById(R.id.tvusertype);
        PgOwner pgOwner=pgOwners.get(position);


                tvname.setText(pgOwner.getUsername());
                tvmobile.setText(pgOwner.getMobile());tvemail.setText(pgOwner.getEmail());tvusertype.setText(pgOwner.getUsertype());




        return convertView;
    }
}
