package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class openhour extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();
    EditText editText2, editText1;
    Button button1;
    String starttime,endtime;

    DateFormat formatter = new SimpleDateFormat("HH:mm"); // Make sure user insert date into edittext in this format.

    Date timeObject1,timeObject2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openhour);
        editText1=findViewById(R.id.starttime);
        editText2= findViewById(R.id.endtime);
        button1 = findViewById(R.id.changetime);
        TimePickerDialog.OnTimeSetListener time1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE,i1);
                updateLabel1();

            }


        };

        TimePickerDialog.OnTimeSetListener time2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE,i1);
                updateLabel2();
            }


        };
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(openhour.this,time1,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(openhour.this,time2,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE), true).show();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1 = editText1.getText().toString();
                String value2 = editText2.getText().toString();
                Log.d("mytag",value1);
                if ((!value1.equals(""))&& !value2.equals("") ){
                        /*TextView testView = findViewById(R.id.holidaytextView);
                        testView.setText("It's weekend day");*/

                        try {
                            timeObject1 = formatter.parse(value1);
                            timeObject2 = formatter.parse(value2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        starttime = new SimpleDateFormat("HH:mm").format(timeObject1);
                        endtime = new SimpleDateFormat("HH:mm").format(timeObject2);
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formbody= new FormBody.Builder().add("start",value1)
                                .add("end",value2)
                                .build();
                        Request request = new Request.Builder().url("http://192.168.56.1:5000/changetime").post(formbody).build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(openhour.this, "network not found", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                TextView testView = findViewById(R.id.timetextView);
                                testView.setText(response.body().string());
                            }
                        });


                }else{
                    Toast.makeText(openhour.this, "Field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLabel1(){
        String myFormat="HH:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText1.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateLabel2(){
        String myFormat="HH:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText2.setText(dateFormat.format(myCalendar.getTime()));
    }
}