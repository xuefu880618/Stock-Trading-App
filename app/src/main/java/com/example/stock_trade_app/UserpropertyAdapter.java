package com.example.stock_trade_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserpropertyAdapter extends ArrayAdapter<Userproperty> {
    private int resourceLayout;
    private Context mContext;

    public UserpropertyAdapter(Context context, int resource, ArrayList<Userproperty> arrayList) {
        super(context, resource, arrayList);
        this.resourceLayout = resource;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Userproperty p = getItem(position);

        if (p != null) {

            TextView tt11 = (TextView) v.findViewById(R.id.stock_symbol2);
            TextView tt22 = (TextView) v.findViewById(R.id.share1);



            if (tt11 != null) {
                tt11.setText(p.getStock_symbol());
                Log.d("mytag",p.getStock_symbol());
            }

            if (tt22 != null) {
                tt22.setText(Double.toString(p.getShares()));
            }

        }

        return v;
    }
}