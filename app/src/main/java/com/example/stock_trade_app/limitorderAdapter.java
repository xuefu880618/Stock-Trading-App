package com.example.stock_trade_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class limitorderAdapter extends ArrayAdapter<Limitorder> {

    private int resourceLayout;
    private Context mContext;



    public limitorderAdapter(Context context, int resource, ArrayList<Limitorder> arrayList) {
        super(context, resource, arrayList);
        this.resourceLayout = resource;
        this.mContext = context;
        Log.d("mytag","hihi");
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }
        Log.d("mytag","hihi");
        Limitorder p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id1);
            TextView tt2 = (TextView) v.findViewById(R.id.limtsymbol1);
            TextView tt3 = (TextView) v.findViewById(R.id.buy2);
            TextView tt4 = (TextView) v.findViewById(R.id.share2);
            TextView tt5 = (TextView) v.findViewById(R.id.price1);
            TextView tt6 = (TextView) v.findViewById(R.id.buytime1);
            Log.d("mytag","hihi");
            String id = "ID: ";
            if (tt1 != null) {
                Log.d("mytag","hihi");
                tt1.setText(id + Integer.toString(p.getId1()));
            }

            String stocksymbol = "stock symbol";
            if (tt2 != null) {
                Log.d("mytag2","hihi");
                tt2.setText(p.getStock_symbol1());
            }
            String state = "State:";
            if (tt3 != null) {
                Log.d("mytag3","hihi");
                if(p.getBuy1() == 1){
                    tt3.setText("Buy");
                }else{
                    tt3.setText("Sell");
                }

            }
            String lowstring = "Share";
            if (tt6 != null) {
                Log.d("mytag4","hihi");
                tt6.setText(Integer.toString(p.getShare()));
            }
            String curstring = "Current price:";
            if (tt4 != null) {
                Log.d("mytag5","hihi");
                tt4.setText(Float.toString(p.getCurprice1()));
            }
            String time = "Buy time:";
            if (tt5 != null) {
                Log.d("mytag6","hihi");
                tt5.setText(p.getDate());
            }

        }

        return v;
    }
}