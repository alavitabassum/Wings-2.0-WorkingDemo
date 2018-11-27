package com.example.user.paperflyv0;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UpdateAssigns extends AppCompatActivity  {

    RecyclerView recyclerView_ua;
    RecyclerView.LayoutManager layoutManager_ua;
    RecyclerView.Adapter adapter_ua;
    List<UpdateAssign_Model> updateAssignModelList;
    Database database;
    private UpdateAssignsAdapter updateAssignsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database=new Database(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_assigns);
        updateAssignModelList = new ArrayList<>();
        TextView mName = findViewById(R.id.merchant_name_update);
        mName.setText(getIntent().getStringExtra("MERCHANTNAME"));
        final String merchantcode = getIntent().getStringExtra("MERCHANTCODE");


        recyclerView_ua = (RecyclerView) findViewById(R.id.recycler_view_ua);
        recyclerView_ua.setHasFixedSize(true);
        layoutManager_ua = new LinearLayoutManager(this);
        recyclerView_ua.setLayoutManager(layoutManager_ua);
        getData(merchantcode);
    }


    private void getData(final String merchantcode)
    {
        try{

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.getassignedexecutive(sqLiteDatabase,merchantcode);
            while (c.moveToNext())
            {
                String rowid = c.getString(0);
                String name = c.getString(1);
                String empcode = c.getString(2);
                String count = c.getString(3);

                UpdateAssign_Model updateAssign_model = new UpdateAssign_Model(rowid,name,count,empcode);
                updateAssignModelList.add(updateAssign_model);
            }
            updateAssignsAdapter = new UpdateAssignsAdapter(updateAssignModelList, getApplicationContext(), merchantcode, new UpdateAssignsAdapter.OnEditTextChanged() {
                @Override
                public void onTextChanged(int position, String charSeq) {


                }
            });
            recyclerView_ua.setAdapter(updateAssignsAdapter);


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error" ,Toast.LENGTH_SHORT).show();
        }
    }


}
