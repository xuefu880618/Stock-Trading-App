package com.example.stock_trade_app;

public class Limitorder {
    int id;
    String username;
    String stock_symbol;
    int buy;
    int share;
    float curprice;
    String date;

    public Limitorder(Object id1, Object username1, Object stock_symbol1, Object buy1, Object share1, Object curprice1,  Object date1) {


        this.id = (Integer)(id1);
        this.username = String.valueOf(username1);
        this.stock_symbol = String.valueOf(stock_symbol1);
        this.curprice = ((Double)curprice1).floatValue();
        this.buy =  (Integer)(buy1);
        this.share =  (Integer)share1;;
        this.date = String.valueOf(date1);
    }
    public int getId1() {
        return id;
    }
    public String getUsername1() {
        return username;
    }
    public String getStock_symbol1() {
        return stock_symbol;
    }
    public float getCurprice1() {
        return curprice;
    }
    public  int getBuy1(){
        return  buy;
    }
    public  int getShare(){
        return  share;
    }
    public String getDate() {
        return date;
    }

}
