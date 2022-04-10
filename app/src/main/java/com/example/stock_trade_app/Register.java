package com.example.stock_trade_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class Register extends AppCompatActivity {
    EditText fullName,email,password,username;

    Integer admin1 = 0;
    Button goToLogin;
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        username = findViewById(R.id.registerUsername);
        CheckBox admin = findViewById((R.id.isAdmin));
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    // Do your coding
                    admin1 = 1;
                }
                else{
                    // Do your coding
                    admin1 = 0;
                }
            }
        });
        goToLogin = findViewById(R.id.gotoLogin);

        checkField(fullName);
        checkField(email);
        checkField(password);

        Button button1= (Button)findViewById(R.id.registerBtn);
        Button button2 = (Button)findViewById((R.id.gotoLogin));
        //Log.d("mytag",admin1.toString());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1 = fullName.getText().toString();
                String value2 = email.getText().toString();
                String value3 = password.getText().toString();
                String value4 = username.getText().toString();
                String value5 = admin1.toString();

                if(!value1.equals("") && !value2.equals("") && !value3.equals("") && !value4.equals("") && !value5.equals("") ){
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody= new FormBody.Builder().add("fullName",value1)
                            .add("email",value2).add("password",value3).add("username",value4)
                            .add("admin1",value5).build();


                    Request request = new Request.Builder().url("http://192.168.56.1:5000/register").post(formbody).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, "network not found", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            TextView testView = findViewById(R.id.textview);
                            testView.setText(response.body().string());

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



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openloginpage();
            }
        });

    }

    public void openloginpage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity((intent));
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