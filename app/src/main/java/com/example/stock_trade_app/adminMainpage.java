package com.example.stock_trade_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminMainpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mainpage);
        Intent intent = getIntent();
        String username = intent.getStringExtra(("key"));
        Button button1=findViewById(R.id.adminAddstock);
        Button button2 = findViewById(R.id.adminAddcloseday);
        Button button3 = findViewById(R.id.settime);
        Button button4 = findViewById(R.id.adminlogout);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openaddstockpage(username);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openaddholidaypage(username);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opensettime(username);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout(username);
            }
        });
    }

    public void openaddstockpage(String passusername){
        Intent intent = new Intent(this, addStock.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
    }
    public void openaddholidaypage(String passusername){
        Intent intent = new Intent(this, addHoliday.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
    }
    public void opensettime(String passusername){
        Intent intent = new Intent(this, openhour.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
    }
    public void logout(String passusername){
        finishAffinity();
        startActivity(new Intent(this, userMainpage.class));
    }
}