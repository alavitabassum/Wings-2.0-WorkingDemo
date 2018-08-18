package com.example.user.paperflyv0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class Testing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        //WebView web = findViewById(R.id.webviewOrderDetails);
       // web.loadUrl("http://paperflywings.com/orders");

        Bundle bundle = getIntent().getExtras();

        String time_new = bundle.getString("newdatatime");

        TextView tv = findViewById(R.id.textView5);

        tv.setText(time_new);
    }
}
