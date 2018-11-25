package com.example.user.paperflyv0;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewAssigns extends AppCompatActivity {

    RecyclerView recyclerView_va;
    RecyclerView.LayoutManager layoutManager_va;
    RecyclerView.Adapter adapter_va;
    List<ViewAssign_Model> viewAssignModelList;
    Database database;
    private ViewAssignsAdapter viewAssignsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database=new Database(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_assigns);

        viewAssignModelList = new ArrayList<>();
        TextView mName = findViewById(R.id.merchant_name_view);
        mName.setText(getIntent().getStringExtra("MERCHANTNAME"));
        final String merchantcode = getIntent().getStringExtra("MERCHANTCODE");


        recyclerView_va = (RecyclerView) findViewById(R.id.recycler_view_va);
        recyclerView_va.setHasFixedSize(true);
        layoutManager_va = new LinearLayoutManager(this);
        recyclerView_va.setLayoutManager(layoutManager_va);
         getData(merchantcode);
    }

    public void getData(final String merchantcode)
    {
        try{

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.getassignedexecutive(sqLiteDatabase,merchantcode);
            while (c.moveToNext())
            {
                String name = c.getString(1);
                String count = c.getString(3);
                ViewAssign_Model viewAssign_model = new ViewAssign_Model(name,count);
                viewAssignModelList.add(viewAssign_model);
            }
            viewAssignsAdapter = new ViewAssignsAdapter(viewAssignModelList, getApplicationContext());

            recyclerView_va.setAdapter(viewAssignsAdapter);


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error" ,Toast.LENGTH_SHORT).show();
        }
    }


}
