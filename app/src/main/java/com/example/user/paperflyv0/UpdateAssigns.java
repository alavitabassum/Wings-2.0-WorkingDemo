package com.example.user.paperflyv0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class UpdateAssigns extends AppCompatActivity {

    RecyclerView recyclerView_ua;
    RecyclerView.LayoutManager layoutManager_ua;
    RecyclerView.Adapter adapter_ua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_assigns);

        TextView mName = findViewById(R.id.merchant_name_update);
        mName.setText(getIntent().getStringExtra("MERCHANTNAME"));


        recyclerView_ua = (RecyclerView) findViewById(R.id.recycler_view_ua);

        layoutManager_ua = new LinearLayoutManager(this);
        recyclerView_ua.setLayoutManager(layoutManager_ua);

        adapter_ua = new UpdateAssignsAdapter();
        recyclerView_ua.setAdapter(adapter_ua);
    }
}
