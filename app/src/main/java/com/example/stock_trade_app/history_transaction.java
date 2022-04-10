package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class history_transaction extends AppCompatActivity {

    ListView listView1;
    private Timer timer = null;
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction);
        Intent  intent = getIntent();
        String username = intent.getStringExtra(("key"));
        int delay = 0; // delay for 0 sec.
        int period = 3000; // repeat every 10 sec.

        Button button1=findViewById(R.id.USER_MAINPAGE);
        Button button2=findViewById(R.id.adminAddcloseday);
        Button button3=findViewById(R.id.Buy);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new history_transaction.displaystock(),delay, period);
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
                    Request request = new Request.Builder().url("http://192.168.56.1:5000/history").post(formbody).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(history_transaction.this, "network not found", Toast.LENGTH_SHORT).show();
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
                                    jarray = jobject.getJSONArray("history");
                                    JSONArray finalJarray = jarray;
                                    //Log.d("mytag1",jsonData);
                                    //Log.d("mytag3",jarray.());
                                    //Log.d("mytag2",finalJarray.getJSONObject(0).get("id").getClass().toString());
                                    JSONArray finalJarray1 = jarray;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayList<History> arrayList = new ArrayList<History>();
                                            try {
                                                for (int i=0, l=finalJarray.length(); i<l; i++){
                                                    arrayList.add(new History(finalJarray.getJSONObject(i).get("id"),finalJarray.getJSONObject(i).get("username"),finalJarray.getJSONObject(i).get("stock_symbol"),
                                                            finalJarray.getJSONObject(i).get("buy_price"),finalJarray.getJSONObject(i).get("buy"),finalJarray.getJSONObject(i).get("shares"),
                                                            finalJarray.getJSONObject(i).get("buy_time")));
                                                    Log.d("mytag",finalJarray.getJSONObject(i).get("stock_symbol").toString());
                                                }
                                            } catch (JSONException e) {}
                                            listView1= (ListView)findViewById(R.id.historylistview);
                                            HistoryAdapter adapter = new HistoryAdapter (history_transaction.this,R.layout.activity_history_adapter,arrayList);//jArray is your json array

                                            //Set the above adapter as the adapter of choice for our list
                                            listView1.setAdapter(adapter);
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
    public void openhistory(){
        Intent intent = new Intent(this, Deposit_Withdraw.class);
        startActivity((intent));
    }
    public void openuserMainpage(String passusername){
        Intent intent = new Intent(this, userMainpage.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
    }
}