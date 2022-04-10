package com.example.stock_trade_app;

import java.text.DateFormat;

public class History {
    int id;
    String username;
    String buy;
    int shares;
    String stock_symbol;
    float price;
    java.util.Date dt = new java.util.Date();

    java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String datetime = sdf.format(dt);

    public History(Object id1, Object username1, Object stock_symbol1,Object price1,Object buy1 , Object shares1, Object datetime1) {


        this.id = (Integer)(id1);
        this.username = String.valueOf(username1);
        this.stock_symbol = String.valueOf(stock_symbol1);
        this.shares = (int) shares1;
        this.buy =  String.valueOf(buy1);
        if(buy.equals("1")){
            buy = "Buy";
        }else{
            buy = "Sell";
        }
        this.price =  ((Double)price1).floatValue();
        this.datetime = String.valueOf(datetime1);
    }
    public int getId() {
        return id;
    }

    // getter method for returning the ID of the TextView 1
    public String getUsername() {
        return username;
    }

    // getter method for returning the ID of the TextView 2
    public String getStock_symbol() {
        return stock_symbol;
    }


    // getter method for returning the ID of the TextView 1
    public String getBuy() {
        return buy;
    }

    // getter method for returning the ID of the TextView 2
    public String getDatetime() {
        return datetime;
    }

    public float getPrice() {
        return price;
    }
    public  int getShares(){
        return  shares;
    }

}
