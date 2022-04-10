package com.example.stock_trade_app;

import java.util.ArrayList;

public class Userproperty {

    Double shares;
    String stock_symbol;



    public Userproperty(Object stock_symbol1, Object shares1) {



        this.stock_symbol = String.valueOf(stock_symbol1);
        this.shares = (Double)shares1;

    }




    // getter method for returning the ID of the TextView 2
    public String getStock_symbol() {
        return stock_symbol;
    }

    public  Double getShares(){
        return  shares;
    }
}
