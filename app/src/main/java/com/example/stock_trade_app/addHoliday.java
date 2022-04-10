package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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

public class addHoliday extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();
    EditText editText, editText1;
    Button button1,button2;
    String date1;


    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.

    Date dateObject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holiday);
        editText=findViewById(R.id.editTextDate2);
        editText1= findViewById(R.id.holidayname);
        button1 = findViewById(R.id.addholiday);
        button2 = findViewById(R.id.deletelimitorder);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addHoliday.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1 = editText.getText().toString();
                String value2 = editText1.getText().toString();
                if ((!value1.equals(""))&& !value2.equals("") ){
                    if ((myCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                            || (myCalendar.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY)) {  //or sunday
                        TextView testView = findViewById(R.id.holidaytextView);
                        testView.setText("It's weekend day");
                    }else {
                        try {
                            dateObject = formatter.parse(value1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        date1 = new SimpleDateFormat("yyyy/MM/dd").format(dateObject);

                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formbody= new FormBody.Builder().add("date",value1)
                                .add("holidayname",value2).add("operation","1")
                                .build();
                        Request request = new Request.Builder().url("http://192.168.56.1:5000/addholiday").post(formbody).build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(addHoliday.this, "network not found", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                TextView testView = findViewById(R.id.holidaytextView);
                                testView.setText(response.body().string());
                            }
                        });
                    }

                }else{
                    Toast.makeText(addHoliday.this, "Field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String value1 = editText.getText().toString();
                String value2 = editText1.getText().toString();
                if ((!value2.equals(""))){


                        //date1 = new SimpleDateFormat("yyyy/MM/dd").format(dateObject);

                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formbody= new FormBody.Builder()
                                .add("holidayname",value2).add("operation","0")
                                .build();
                        Request request = new Request.Builder().url("http://192.168.56.1:5000/addholiday").post(formbody).build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(addHoliday.this, "network not found", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                TextView testView = findViewById(R.id.holidaytextView);
                                testView.setText(response.body().string());
                            }
                        });


                }else{
                    Toast.makeText(addHoliday.this, "Field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateLabel(){
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }

}