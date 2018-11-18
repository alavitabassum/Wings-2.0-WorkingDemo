package com.example.user.paperflyv0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UpdateAssigns extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_assigns_layout);

        TextView mName = findViewById(R.id.merchant_name_update);
        mName.setText(getIntent().getStringExtra("MERCHANTNAME"));
    }
}
