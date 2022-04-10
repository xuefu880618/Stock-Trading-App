package com.example.stock_trade_app;
import com.google.gson.*;

public class Stock {

    int id;
    String company;
    String stock_symbol;
    float curprice;
    float openprice;
    float highprice;
    float lowprice;
    Long volume;
    Double cap;




    public Stock(Object id1, Object company1, Object stock_symbol1, Object cur_price1, Object open_price1, Object high_price1, Object low_price1, Object volume1) {


        this.id = (Integer)(id1);
        this.company = String.valueOf(company1);
        this.stock_symbol = String.valueOf(stock_symbol1);
        this.curprice = ((Double)cur_price1).floatValue();
        this.openprice =  ((Double)open_price1).floatValue();
        this.highprice =  ((Double)high_price1).floatValue();
        this.lowprice =  ((Double)low_price1).floatValue();
        this.volume = ((Number) volume1).longValue();

    }


    public int getId() {
        return id;
    }

    // getter method for returning the ID of the TextView 1
    public String getCompany() {
        return company;
    }

    // getter method for returning the ID of the TextView 2
    public String getStock_symbol() {
        return stock_symbol;
    }


    // getter method for returning the ID of the TextView 1
    public float getCurprice() {
        return curprice;
    }

    // getter method for returning the ID of the TextView 2
    public float getOpenprice() {
        return openprice;
    }

    public float getHighprice() {
        return highprice;
    }
    public float getLowprice() {
        return lowprice;
    }
    public  Long getVolume(){
        return  volume;
    }



}
