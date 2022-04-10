package com.example.stock_trade_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StockAdapter extends ArrayAdapter<Stock> {

    private int resourceLayout;
    private Context mContext;

   public StockAdapter(Context context, int resource,ArrayList<Stock> arrayList) {
        super(context, resource, arrayList);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    /*public StockAdapter(@NonNull Context context, ArrayList<Stock> arrayList) {
        super(context, 0, arrayList);
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Stock p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.stock_symbol);
            TextView tt2 = (TextView) v.findViewById(R.id.company);
            TextView tt3 = (TextView) v.findViewById(R.id.open_price);
            TextView tt4 = (TextView) v.findViewById(R.id.cur_price);
            TextView tt5 = (TextView) v.findViewById(R.id.high_price);
            TextView tt6 = (TextView) v.findViewById(R.id.low_price);
            TextView tt7 = (TextView) v.findViewById(R.id.cap);
            TextView tt8 = (TextView) v.findViewById(R.id.volume);

            if (tt1 != null) {
                tt1.setText(p.getStock_symbol());
            }

            if (tt2 != null) {
                tt2.setText(p.getCompany());
            }
            String openstirng = "Open price:";
            if (tt3 != null) {
                tt3.setText( openstirng + Float.toString(p.getOpenprice()));
            }
            String curstring = "Current price:";
            if (tt4 != null) {
                tt4.setText(curstring + Float.toString(p.getCurprice()));
            }
            String highstring = "High price:";
            if (tt5 != null) {
                tt5.setText(highstring + Float.toString(p.getHighprice()));
            }
            String lowstring = "Low price:";
            if (tt6 != null) {
                tt6.setText(lowstring + Float.toString(p.getLowprice()));
            }
            String capstirng = "Volume:";
            Long getcapstirng =p.getVolume();
            if (tt7 != null) {
                tt7.setText( capstirng +  getcapstirng.toString()  );
            }
            if (tt8 != null) {
                tt8.setText( "Cap: " + Double.toString(getcapstirng*p.getCurprice()));
            }
        }

        return v;
    }

}