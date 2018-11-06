package com.example.user.paperflyv0;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button signin;
    EditText username,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin = (Button) findViewById(R.id.sign_in);
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate(username.getText().toString(),pass.getText().toString());

            }
        });


    }
    public void validate(String userName, String userPass){
        if((userName.equals("haris"))&& (userPass.equals("1234"))) {
            Intent m_menu_intent = new Intent(LoginActivity.this,
                    ManagerCardMenu.class);
            startActivity(m_menu_intent);
        }
        else if ((userName.equals("tonoy"))&& (userPass.equals("1234"))){
            Intent e_menu_intent = new Intent(LoginActivity.this,
                    ExecutiveCardMenu.class);
            startActivity(e_menu_intent);
        }

    }


}
