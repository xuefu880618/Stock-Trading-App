package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class addStock extends AppCompatActivity {
    EditText company,stocksymbol,openprice,volume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        company = findViewById(R.id.company_name);
        stocksymbol = findViewById(R.id.holidayname);
        openprice = findViewById(R.id.openprice);
        volume = findViewById(R.id.volumenumber);
        Button button1= (Button)findViewById(R.id.changetime);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1 = company.getText().toString();
                String value2 = stocksymbol.getText().toString();
                String value3 = openprice.getText().toString();
                String value4 = volume.getText().toString();
                if(!value1.equals("") && !value2.equals("") && !value3.equals("") && !value4.equals("")){
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody= new FormBody.Builder().add("company",value1)
                            .add("stocksymbol",value2).add("openprice",value3).add("volume",value4)
                            .build();

                    Request request = new Request.Builder().url("http://192.168.56.1:5000/addstock").post(formbody).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(addStock.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            TextView testView = findViewById(R.id.admintextview);
                            testView.setText(response.body().string());
                        }
                    });
                }else{
                    Toast.makeText(addStock.this, "Field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}