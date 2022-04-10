package com.example.stock_trade_app;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class userMainpage extends AppCompatActivity {
    ListView listView1;
    boolean valid = true;
    private Timer timer = null;
    private Handler handler = new Handler();
    TextView cashamount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermainpage);
        Intent intent = getIntent();
        String username = intent.getStringExtra(("key"));
        Log.d("mytag",username);
        int delay = 0; // delay for 0 sec.
        int period = 3000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new userMainpage.displayproperty(),delay, period);
        //Log.d("mytag",username);
        Button button1=findViewById(R.id.USER_MAINPAGE);
        Button button2=findViewById(R.id.adminAddcloseday);
        Button button3=findViewById(R.id.Buy);
        Button button4=findViewById(R.id.Transaction);
        Button button5=findViewById(R.id.logout);
        cashamount = findViewById(R.id.cash_amount);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openstockpage(username);
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
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(username);
            }
        });


    }
    class displayproperty extends TimerTask {
        @Override
        public void run() {


            handler.post(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();

                    Intent intent = getIntent();
                    String username = intent.getStringExtra(("key"));
                    RequestBody formbody= new FormBody.Builder().add("username",username)
                            .build();
                    Request request = new Request.Builder().url("http://192.168.56.1:5000/property").post(formbody).build();
                    Request request1 = new Request.Builder().url("http://192.168.56.1:5000/cashamount").post(formbody).build();
                    Log.d("histag",username);
                    okHttpClient.newCall(request1).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(userMainpage.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()){
                                String cashstirng = "Your cash amount: ";
                                cashamount.setText(cashstirng + response.body().string());
                            }
                        }
                    });
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(userMainpage.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                            if (response.isSuccessful()){
                                JSONArray jarray = new JSONArray();
                                Gson gson = new Gson();
                                try{
                                    final String jsonData = response.body().string().trim();
                                    JSONObject jobject = new JSONObject(jsonData);
                                    jarray = jobject.getJSONArray("userproperty");
                                    JSONArray finalJarray = jarray;
                                    final String[] cash_amount = new String[1];
                                    //Log.d("mytag1",jsonData);
                                    //Log.d("mytag3",jarray.());
                                    Log.d("mytag2",finalJarray.toString());
                                    runOnUiThread(new Runnable() {
                                        @Override


                                        public void run() {
                                            ArrayList<Userproperty> arrayList = new ArrayList<Userproperty>();
                                            try {
                                                for (int i=0, l=finalJarray.length(); i<l; i++){
                                                    arrayList.add(new Userproperty(finalJarray.getJSONObject(i).get("stock_symbol"),
                                                            finalJarray.getJSONObject(i).get("shares")));
                                                    //cash_amount[0] = finalJarray.getJSONObject(i).get("cashamount").toString();
                                                    //Log.d("mytag", cash_amount[0]);

                                                }
                                            } catch (JSONException e) {}
                                            listView1= (ListView)findViewById(R.id.userlistview);
                                            UserpropertyAdapter adapter = new UserpropertyAdapter (userMainpage.this,R.layout.activity_userproperty_adapter,arrayList);//jArray is your json array

                                            //Set the above adapter as the adapter of choice for our list
                                            listView1.setAdapter(adapter);
                                            //String cashstirng = "Your cash amount: ";
                                            //cashamount.setText(cashstirng + cash_amount[0]);


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
    public void openstockpage(String passusername){
        Intent intent = new Intent(this, Stockmarket.class);
        intent.putExtra("key", passusername);
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

    public void logout(String passusername){
        finishAffinity();
        startActivity(new Intent(this, userMainpage.class));
    }

}

