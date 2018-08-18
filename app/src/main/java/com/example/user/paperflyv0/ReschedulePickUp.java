package com.example.user.paperflyv0;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReschedulePickUp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button btn_editSchedule;
    Button btn_confirmUpdate;
    TextView rescheduleOutput;

    int day,month,year,hour, minute;
    int dayUpdated, monthUpdated, yearUpdated, hourUpdated, minuteUpdated;
    private String Datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_pick_up);

        btn_editSchedule =findViewById(R.id.update_date_time);
        btn_confirmUpdate =findViewById(R.id.confirm_update);
        rescheduleOutput =findViewById(R.id.reschedule_output);


        btn_editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c =Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReschedulePickUp.this,
                        ReschedulePickUp.this,year,month,day);
                datePickerDialog.show();
            }
        });


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    yearUpdated =i;
    monthUpdated = i1+2;
    dayUpdated = i2;

    Calendar c = Calendar.getInstance();
    hour =c.get(Calendar.HOUR_OF_DAY);
    minute = c.get(Calendar.MINUTE);


        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
       // Datetime = sdf.format(c.getTime());


    TimePickerDialog timePickerDialog = new TimePickerDialog(ReschedulePickUp.this,
            ReschedulePickUp.this,hour,minute, DateFormat.is24HourFormat(this));
 timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {


        hourUpdated  = i;
        minuteUpdated = i1;


        rescheduleOutput.setText("New PickUp Date: "+yearUpdated+":"+monthUpdated+":"+dayUpdated + "\n" +
                "New PickUp Time: " + hourUpdated + ":" + minuteUpdated );
    }



    public void passData(View view){
//
//Button btn_status =findViewById(R.id.button_status);

    // btn_status.setBackgroundColor(getResources().getColor(R.color.yellow));
//String time_new = "testing";
//String status_new = "texting 2";
Intent passData_intent = new Intent (ReschedulePickUp.this,PickUpActivity.class);
//
//Bundle bundle = new Bundle();
//
//bundle.putString("newdatatime",time_new);
//bundle.putString("newdatastatus", status_new);
//
//passData_intent.putExtras(bundle);

//passData_intent.putExtra("newdatatime",time_new);


startActivity(passData_intent);
finish();
    }


}
