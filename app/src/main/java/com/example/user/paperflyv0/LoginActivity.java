package com.example.user.paperflyv0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button orderlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        orderlist = findViewById(R.id.sign_in);


        // Capture button clicks
        orderlist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(LoginActivity.this,
                        Welcome.class);
                startActivity(myIntent);
            }
        });



    }

}
