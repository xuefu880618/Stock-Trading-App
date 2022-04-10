package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Deposit_Withdraw extends AppCompatActivity {
    EditText money;
    Button deposit,withdraw;
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_withdraw);
        money = findViewById(R.id.money);
        deposit = findViewById(R.id.deposit);
        withdraw = findViewById(R.id.withdraw);
        Intent intent = getIntent();
        String username = intent.getStringExtra(("key"));
        checkField(money);

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1 = money.getText().toString();
                String value2 = "deposit";
                if (value1 != null && username != null) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody = new FormBody.Builder().add("money", value1)
                            .add("operation", value2).add("username",username).build();

                    Request request = new Request.Builder().url("http://192.168.56.1:5000/daw").post(formbody).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Deposit_Withdraw.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String responseBodyCopy = response.peekBody(Long.MAX_VALUE).string();

                            if (responseBodyCopy.equals("deposit success!")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "deposit success!", Toast.LENGTH_SHORT).show();

                                    }

                                });

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        }

                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "field cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1 = money.getText().toString();
                String value2 = "withdraw";
                if (value1 != null) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody = new FormBody.Builder().add("money", value1)
                            .add("operation", value2).add("username",username).build();

                    Request request = new Request.Builder().url("http://192.168.56.1:5000/daw").post(formbody).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Deposit_Withdraw.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String responseBodyCopy = response.peekBody(Long.MAX_VALUE).string();

                            if (responseBodyCopy.equals("withdraw success!")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "withdraw success!", Toast.LENGTH_SHORT).show();

                                    }

                                });

                            } else if (responseBodyCopy.equals("you do not have enough money")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "you do not have enough money", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        }

                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "field cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}