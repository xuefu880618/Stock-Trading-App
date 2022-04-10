package com.example.stock_trade_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Button loginBtn,gotoRegister;
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        username = findViewById(R.id.username);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button1=findViewById(R.id.loginBtn);
        Button button2 = findViewById((R.id.gotoRegister));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openregisterpage();
            }
        });

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                EditText username = findViewById(R.id.username);
                EditText password = findViewById(R.id.loginPassword);

                String value1 = username.getText().toString();
                String value2 = password.getText().toString();

                if(value1 != null && value2 != null){
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody= new FormBody.Builder().add("username",value1)
                            .add("password",value2).build();


                    Request request = new Request.Builder().url("http://192.168.56.1:5000/login").post(formbody).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String responseBodyCopy = response.peekBody(Long.MAX_VALUE).string();

                            if(responseBodyCopy.equals("Logged in as user")){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Logged in as user", Toast.LENGTH_SHORT).show();
                                        openuserMainpage(value1);
                                    }

                                });

                            }else if(responseBodyCopy.equals("Logged in as admin")){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Logged in as admin", Toast.LENGTH_SHORT).show();
                                        openadminMainpage();
                                    }

                                });

                            }else if(responseBodyCopy.equals("Incorrect username/password!")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Incorrect username/password!", Toast.LENGTH_SHORT).show();
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

    public void openregisterpage(){
        Intent intent = new Intent(this, Register.class);
        startActivity((intent));
    }
    public void openuserMainpage(String passusername){
        Intent intent = new Intent(this, userMainpage.class);
        intent.putExtra("key", passusername);
        startActivity((intent));
        finish();
    }
    public void openadminMainpage(){
        Intent intent = new Intent(this, adminMainpage.class);
        startActivity((intent));
        finish();
    }

}
