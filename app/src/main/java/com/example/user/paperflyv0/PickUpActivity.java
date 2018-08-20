package com.example.user.paperflyv0;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PickUpActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressBar progressBar;
    ListView lst;
    ListView listView;
    ImageView imgvw;
    TextView changeStatus;
    TextView new_ptime;
    int check=0;
    EditText pr_input;

    android.widget.RelativeLayout vwParentRow;

    //variables for Calender and Clock
    int day,month,year,hour, minute;
    int dayUpdated, monthUpdated, yearUpdated, hourUpdated, minuteUpdated;
    private String Datetime;

    List<Pickup> pickupList;
//
//    private SwipeRefreshLayout refreshLayout;
//    private int refresh_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);



      // Button btn_status = findViewById(R.id.button_status);

       // Bundle bundle = getIntent().getExtras();
        //pass data to String variables
        //String time_new = bundle.getString("newdatatime");
       // String status_new = bundle.getString("newdatastatus");

        //assign xml objects for Textviews into variable

      //  changeStatus =findViewById(R.id.chng_status);
        //new_ptime =findViewById(R.id.ptime);

        //changeStatus.setText(time_new);
      //  new_ptime.setText(status_new);

     //   lst=findViewById(R.id.listview) ;
  //   CustomListview customListview=new CustomListview(this,statusChange);
      // lst.setAdapter(customListview);

        // Textview to update pickup status
    // changeStatus= findViewById(R.id.chng_status);
//        registerForContextMenu(changeStatus);

        //refresh layout
//         refreshLayout=findViewById(R.id.swipe_refresh);

        //listView of pickups
        pickupList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview);

        readHeroes();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    //    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//
//
//refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//    @Override
//    public void onRefresh() {
//
//    }
//});
//


    }



//    public void refreshItem()
//    {
//        switch (refresh_count)
//        {
//
//        }
//
//        refresh_count++;
//    }

    private void readHeroes() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_HEROES, null, CODE_GET_REQUEST);
        request.execute();
    }

    class PickupAdapter extends ArrayAdapter<Pickup> {
        List<Pickup> pickupList;

        public PickupAdapter(List<Pickup> pickupList) {
            super(PickUpActivity.this, R.layout.listview_layout, pickupList);
            this.pickupList = pickupList;
//
//            if (refresh_count>3)
//            {
//                refresh_count = 0;
//            }
//            refreshLayout.setRefreshing(false);
        }


        @Override
            public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.listview_layout, null, true);

            final TextView name = listViewItem.findViewById(R.id.name);
            TextView address = listViewItem.findViewById(R.id.add);
            TextView time = listViewItem.findViewById(R.id.ptime);
            imgvw=listViewItem.findViewById(R.id.iconpf);
            Pickup hero = pickupList.get(position);

            name.setText(hero.getMerchantName());
            address.setText(hero.getMerchantAddress());
            time.setText(hero.getScheduleTime());
            imgvw.setImageResource(R.drawable.call);

            //make phone call to merchant
            String phoneNumber = hero.getPhone();
            Log.d("phone",phoneNumber);
            final String uri = "tel:" + phoneNumber;


            imgvw.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    // callIntent.setData(Uri.parse("tel:" +phone));
                    callIntent.setData(Uri.parse(uri));
                    if (ActivityCompat.checkSelfPermission(PickUpActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                }
            });


            return listViewItem;


        }
    }

    private void refreshHeroList(JSONArray heroes) throws JSONException {
        pickupList.clear();

        for (int i = 0; i < heroes.length(); i++) {
            JSONObject obj = heroes.getJSONObject(i);


            pickupList.add(new Pickup(
                    obj.getInt("id"),
                    obj.getString("merchantName"),
                    obj.getString("merchantAddress"),
                    obj.getString("scheduleTime"),
                    obj.getString("phone")
            ));
        }

        PickupAdapter adapter = new PickupAdapter(pickupList);
        listView.setAdapter(adapter);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //     progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList(object.getJSONArray("heroes"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pick_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //app side menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pickDue) {
            // Start class to view orders details
            Intent pickupIntent = new Intent(PickUpActivity.this,
                    PickUpActivity.class);
            startActivity(pickupIntent);
            // Handle the camera action
        } else if (id == R.id.nav_pickCompleted) {

        } else if (id == R.id.nav_exception) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // pop up menu for status options
    public void gotochangestatus(View view){

        vwParentRow = (android.widget.RelativeLayout) view.getParent();
        PopupMenu popup = new PopupMenu(this,view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pickup_status_options);
        popup.show();

    }


//on menuitem click
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();
       final Button btn_status = (Button)vwParentRow.getChildAt(3);

        if (id== R.id.confirm ){
            check = 1;

                        AlertDialog.Builder a_builder =new AlertDialog.Builder(PickUpActivity.this);
                        a_builder.setMessage("Do you want to confirm the Pick Up?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(PickUpActivity.this,"Pick Up Confirmed", Toast.LENGTH_SHORT).show();
                                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.green));
                                btn_status.setText("Confirmed");

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Alert!");
                        alert.show();

        }

        else if (id == R.id.callAgain){
            check = 2;

            Calendar c =Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(PickUpActivity.this,
                    PickUpActivity.this,year,month,day);
            datePickerDialog.show();
        }

        else if (id== R.id.rescheduled)
        {
            check = 3;

            Calendar c =Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(PickUpActivity.this,
                    PickUpActivity.this,year,month,day);
            datePickerDialog.show();
        }

        else if (id == R.id.pendingReview)
        {
            check = 4;
//            AlertDialog.Builder a_builder =new AlertDialog.Builder(PickUpActivity.this);
////            a_builder.setTitle("Enter reason for review");
////            pr_input = new EditText(this);
////            a_builder.setView(pr_input);
////
////            a_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                   String txt = pr_input.getText().toString();
////                        if (txt.isEmpty()){
////                              Toast.makeText(PickUpActivity.this,"Review reason not specified", Toast.LENGTH_SHORT).show();
////                         }else {
////                               Toast.makeText(PickUpActivity.this,"Submitted fro review", Toast.LENGTH_SHORT).show();
////                               btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
////                               btn_status.setText("P. Review");
////                           }
////                }
////            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    dialog.cancel();
////                }
////            });
////            AlertDialog alert = a_builder.create();
////            alert.show();
            AlertDialog.Builder a_builder =new AlertDialog.Builder(PickUpActivity.this);
            a_builder.setMessage("Do you want submit the Pick Up order for review?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Toast.makeText(PickUpActivity.this,"Order Submitted fro Review", Toast.LENGTH_SHORT).show();
                    btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
                    btn_status.setText("R.Review");
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Alert!");
            alert.show();

        }

        else if (id == R.id.cancel)
        {


            check = 5;

            AlertDialog.Builder a_builder =new AlertDialog.Builder(PickUpActivity.this);
            a_builder.setMessage("Do you want to cancel the Pick Up?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Toast.makeText(PickUpActivity.this,"Pick Up Cancelled", Toast.LENGTH_SHORT).show();
                    btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                    btn_status.setText("Cancelled");
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Alert!");
            alert.show();
        }
        vwParentRow.refreshDrawableState();
        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearUpdated =i;
        monthUpdated = i1+2;
        dayUpdated = i2;

        Calendar c = Calendar.getInstance();
        hour =c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(PickUpActivity.this,
                PickUpActivity.this,hour,minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }



    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        final Button btn_status = (Button)vwParentRow.getChildAt(3);
        final TextView new_ptime = (TextView)vwParentRow.getChildAt(5);
        final TextView new_calltime = (TextView)vwParentRow.getChildAt(7);
        hourUpdated  = i;
        minuteUpdated = i1;

        // if call again is selected
        if(check==2){

            new_calltime.setText("Call Again Time: " + hourUpdated + ":" + minuteUpdated + "\n" +"Call Again Date: "+dayUpdated+":"+monthUpdated+":"+yearUpdated);
            btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
            btn_status.setText("Call Again");
            new_calltime.setTextColor(Color.parseColor("#25ade3"));

        }
        //if reschedule is selected
        else if(check==3){

            new_ptime.setText(hourUpdated + ":" + minuteUpdated );
            btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.pfColor));
            btn_status.setText("R.S.");
            new_ptime.setTextColor(Color.parseColor("#e5000b"));
           // new_ptime.setBackgroundColor(Color.parseColor("#ffff2d"));
        }
    }

// view the table layout for order details against respective merchant
    public void gotoOrderDetails(View view){
        // Start class to view orders details
        Intent myIntent = new Intent(PickUpActivity.this,
                OrderDetails.class);
        startActivity(myIntent);
    }



}

