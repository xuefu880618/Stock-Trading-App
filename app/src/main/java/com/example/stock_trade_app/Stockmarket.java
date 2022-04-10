package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Stockmarket extends AppCompatActivity  {

    ListView listView;
    private Timer timer = null;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockmarket);
        int delay = 0; // delay for 0 sec.
        int period = 3000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new displaystock(),delay, period);
        Intent intent = getIntent();
        String username = intent.getStringExtra(("key"));
        Button button1=findViewById(R.id.USER_MAINPAGE);
        Button button2=findViewById(R.id.adminAddcloseday);
        Button button3=findViewById(R.id.Buy);
        Button button4=findViewById(R.id.Transaction);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openuserMainpage(username);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opendepostipage(username);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opendebuypage(username);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openhistory(username);
            }
        });




    }

    public class displaystock extends TimerTask{
        @Override
        public void run() {
            Intent intent = getIntent();

            String username = intent.getStringExtra(("key"));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody= new FormBody.Builder().add("username",username).build();
                    Request request = new Request.Builder().url("http://192.168.56.1:5000/stock").post(formbody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Stockmarket.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull  Response response) throws IOException {

                            if (response.isSuccessful()){
                                JSONArray jarray = new JSONArray();
                                Gson gson = new Gson();
                                try{
                                    final String jsonData = response.body().string().trim();
                                    JSONObject jobject = new JSONObject(jsonData);
                                    jarray = jobject.getJSONArray("company");
                                    JSONArray finalJarray = jarray;
                                    //Log.d("mytag1",jsonData);
                                    //Log.d("mytag3",jarray.());
                                    //Log.d("mytag2",finalJarray.getJSONObject(0).get("id").getClass().toString());
                                    JSONArray finalJarray1 = jarray;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayList<Stock> arrayList = new ArrayList<Stock>();
                                            try {
                                                for (int i=0, l=finalJarray.length(); i<l; i++){
                                                    arrayList.add(new Stock(finalJarray.getJSONObject(i).get("id"),finalJarray.getJSONObject(i).get("company"),finalJarray.getJSONObject(i).get("stock_symbol"),
                                                            finalJarray.getJSONObject(i).get("cur_price"),finalJarray.getJSONObject(i).get("open_price"),finalJarray.getJSONObject(i).get("high_price"),
                                                            finalJarray.getJSONObject(i).get("low_price"),finalJarray.getJSONObject(i).get("volume")));
                                                }
                                            } catch (JSONException e) {}
                                            listView= (ListView)findViewById(R.id.ListView);
                                            StockAdapter adapter = new StockAdapter (Stockmarket.this,R.layout.activity_stock_adapter,arrayList);//jArray is your json array

                                            //Set the above adapter as the adapter of choice for our list
                                            listView.setAdapter(adapter);
                                        }
                                    });
                                }catch(IOException | JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });
        }

    }


    public void openstockpage(){
        Intent intent = new Intent(this, Stockmarket.class);
        startActivity((intent));
        if(timer != null){
            timer.cancel();
        }
    }

    public void opendepostipage(String passusername){
        Intent intent = new Intent(this, Deposit_Withdraw.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
        if(timer != null){
            timer.cancel();
        }
    }
    public void opendebuypage(String passusername){
        Intent intent = new Intent(this, BuyandSell.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
        if(timer != null){
            timer.cancel();
        }
    }
    public void openhistory(String passusername){
        Intent intent = new Intent(this, history_transaction.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
        if(timer != null){
            timer.cancel();
        }
    }
    public void openuserMainpage(String passusername){
        Intent intent = new Intent(this, userMainpage.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
    }
}




