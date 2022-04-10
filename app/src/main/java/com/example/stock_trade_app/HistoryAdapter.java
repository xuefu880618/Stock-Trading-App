package com.example.stock_trade_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<History> {
    private int resourceLayout;
    private Context mContext;

    public HistoryAdapter(Context context, int resource, ArrayList<History> arrayList) {
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

        History p = getItem(position);

        if (p != null) {

            TextView tt11 = (TextView) v.findViewById(R.id.ss1);
            TextView tt22 = (TextView) v.findViewById(R.id.price);
            TextView tt33 = (TextView) v.findViewById(R.id.share);
            TextView tt44 = (TextView) v.findViewById(R.id.buy1);
            TextView tt55 = (TextView) v.findViewById(R.id.buytime);


            if (tt11 != null) {
                tt11.setText(p.getStock_symbol());
                Log.d("mytag",p.getStock_symbol());
            }

            if (tt22 != null) {
                tt22.setText(Float.toString(p.getPrice()));
                Log.d("mytag",Float.toString(p.getPrice()));
            }

            if (tt33 != null) {
                tt33.setText(Double.toString(p.getShares()));
                //Log.d("mytag",Float.toString(p.getShares()));
            }
            if (tt44 != null) {
                tt44.setText(p.getBuy());
                Log.d("mytag",p.getBuy());
            }

            if (tt55 != null) {
                tt55.setText(p.getDatetime());
                Log.d("mytag",p.getDatetime());
            }

        }

        return v;
    }
}