package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BuyandSell extends AppCompatActivity {
    Button button1,button2, button3, button4;
    EditText stocksymbol, share, limitorder,date1;
    Date timeObject1;
    final Calendar myCalendar= Calendar.getInstance();

    //DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyand_sell);
        button1 = findViewById(R.id.buy);
        button2 = findViewById(R.id.sell);
        button3 = findViewById((R.id.userMainpage));
        button4 = findViewById(R.id.viewlimitorder);
        date1=findViewById(R.id.editTextDate);
        Intent intent = getIntent();
        String username = intent.getStringExtra(("key"));

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(BuyandSell.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                timeObject1 = calendar.getTime();
                int day = calendar.get(Calendar.DAY_OF_WEEK);

                //DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timeObject1);
                date1 = findViewById(R.id.editTextDate);
                stocksymbol = findViewById(R.id.inputstocksymbol);
                share = findViewById(R.id.share);
                limitorder = findViewById((R.id.limitorder));
                String value1 = stocksymbol.getText().toString();
                String value2 = share.getText().toString();
                String value3 = limitorder.getText().toString();
                String value4 = "buy";
                String value5 = formattedDate;
                String value6 = date1.getText().toString();


                if(value1 != null && value2 != null){
                    if ((myCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                            || (myCalendar.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY)) {  //or sunday
                        Toast.makeText(BuyandSell.this, "It's weekend", Toast.LENGTH_SHORT).show();
                    }else{
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formbody= new FormBody.Builder().add("stocksymbol",value1)
                                .add("share",value2).add("limitorder",value3).add("operation",value4)
                                .add("buytime",value5).add("username",username)
                                .add("expire_date",value6).build();


                        Request request = new Request.Builder().url("http://192.168.56.1:5000/buyandsell").post(formbody).build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BuyandSell.this, "network not found", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String responseBodyCopy = response.peekBody(Long.MAX_VALUE).string();

                                if(responseBodyCopy.equals("buy stock complete")){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Buy stock complete", Toast.LENGTH_SHORT).show();

                                        }

                                    });

                                }else if(responseBodyCopy.equals("you do not have enough money")){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "you do not have enough money", Toast.LENGTH_SHORT).show();

                                        }

                                    });

                                }else if(responseBodyCopy.equals("buy limit all set!")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Buy limit all set!", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("The stock is not exist")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "The stock is not exist", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("today is not open")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "today is not open", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("today is weekend")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "today is weekend", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("now is not open")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "now is not open", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }



                            }

                        });
                    }


                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "field cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                stocksymbol = findViewById(R.id.inputstocksymbol);
                share = findViewById(R.id.share);
                limitorder = findViewById((R.id.limitorder));
                date1 = findViewById(R.id.editTextDate);
                String value1 = stocksymbol.getText().toString();
                String value2 = share.getText().toString();
                String value3 = limitorder.getText().toString();
                String value4 = "sell";
                String value5 = currentDate;
                String value6 = date1.getText().toString();
                if(value1 != null && value2 != null){
                    if ((myCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                            || (myCalendar.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY)) {  //or sunday
                        Toast.makeText(BuyandSell.this, "It's weekend", Toast.LENGTH_SHORT).show();
                    }else{
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formbody= new FormBody.Builder().add("stocksymbol",value1)
                                .add("share",value2).add("limitorder",value3).add("operation",value4).add("buytime",value5).add("username",username)
                                .add("expire_date",value6).build();


                        Request request = new Request.Builder().url("http://192.168.56.1:5000/buyandsell").post(formbody).build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BuyandSell.this, "network not found", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String responseBodyCopy = response.peekBody(Long.MAX_VALUE).string();

                                if(responseBodyCopy.equals("sell stock complete")){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Sell stock complete", Toast.LENGTH_SHORT).show();

                                        }

                                    });

                                }else if(responseBodyCopy.equals("you do not have enough money")){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "You do not have enough money", Toast.LENGTH_SHORT).show();

                                        }

                                    });

                                }else if(responseBodyCopy.equals("sell limit all set!")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Sell limit all set!", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("The stock is not exist")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "The stock is not exist", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("today is not open")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "today is not open", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("today is weekend")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "today is weekend", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("now is not open")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "now is not open", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else if(responseBodyCopy.equals("there is not that stock in the market")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "there is not that stock in the market", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }



                            }

                        });
                    }


                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "field cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openuserMainpage(username);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLimitorderview(username);
            }
        });


    }

    public void openuserMainpage(String passusername){
        Intent intent = new Intent(this, userMainpage.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
    }
    public void openLimitorderview(String passusername){
        Intent intent = new Intent(this, limitorderview.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
    }
    private void updateLabel(){
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        date1.setText(dateFormat.format(myCalendar.getTime()));
    }
}