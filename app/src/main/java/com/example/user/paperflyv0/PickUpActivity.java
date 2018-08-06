package com.example.user.paperflyv0;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PickUpActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressBar progressBar;
    ListView lst;
    ListView listView;
    ImageView imgvw;


    List<Pickup> pickupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);


        // popup menu for pick up status options


        //listView od pickups
        pickupList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview);

        readHeroes();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void readHeroes() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_HEROES, null, CODE_GET_REQUEST);
        request.execute();
    }

    class PickupAdapter extends ArrayAdapter<Pickup> {
        List<Pickup> pickupList;

        public PickupAdapter(List<Pickup> pickupList) {
            super(PickUpActivity.this, R.layout.listview_layout, pickupList);
            this.pickupList = pickupList;
        }


        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.listview_layout, null, true);

            TextView name = listViewItem.findViewById(R.id.name);
            TextView address = listViewItem.findViewById(R.id.add);
            TextView time = listViewItem.findViewById(R.id.ptime);
            imgvw=listViewItem.findViewById(R.id.imgCall);
            final Button btn_status = (Button)findViewById(R.id.button_status);
            //   final int phone = listViewItem.findViewById(R.id.phone_button);
            final Pickup hero = pickupList.get(position);

            name.setText(hero.getMerchantName());
            address.setText(hero.getMerchantAddress());
            time.setText(hero.getScheduleTime());
            imgvw.setImageResource(R.drawable.call);

            String phoneNumber = hero.getPhone();
            Log.d("phone",phoneNumber);
            final String uri = "tel:" + phoneNumber;

            //btnstatus.setOnCreateContextMenuListener(PickUpActivity.this);
            // pickUpOptions(listViewItem);
            // btn_status.setOnClickListener();

            //     @Override
            /*
            btn_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(PickUpActivity.this, btn_status);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.pickup_status_options, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick (MenuItem item){
                            Button btn_status = findViewById(R.id.button_status);
                            switch (item.getItemId()) {
                                case R.id.confirm:
                                   // Toast.makeText(this, "Pick Up Confirmed", Toast.LENGTH_SHORT).show();
                                    btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.green));
                                    btn_status.setText("Confirmed");
                                    return true;
                                case R.id.callAgain:
                                 //   Toast.makeText(this, "Call Merchant later", Toast.LENGTH_SHORT).show();
                                    btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
                                    btn_status.setText("CallBack");
                                    return true;
                                case R.id.rescheduled:
                                  //  Toast.makeText(this, "Pick Up Rescheduled", Toast.LENGTH_SHORT).show();
                                    btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.pfColor));
                                    btn_status.setText("R.S.");
                                    return true;
                                case R.id.pendingReview:
                                 //   Toast.makeText(this, "Pick Up is pending review", Toast.LENGTH_SHORT).show();
                                    btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
                                    btn_status.setText("Pending");
                                    return true;
                                case R.id.cancel:
                                  //  Toast.makeText(this, "Pick Up Cancelled", Toast.LENGTH_SHORT).show();
                                    btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                                    btn_status.setText("Cancelled");
                                    return true;
                                default:
                                    return false;

                            }

                        }
                    });

                    popup.show(); //showing popup menu
                }
            }); */
/*         int phoneNumber = hero.getPhone();

           final String phone = Integer.toString(phoneNumber);
            Log.d("MYINT", "value: " +phoneNumber);
           // Log.d("phone",phone);
            final String uri = "tel:" + phone;*/
            // Log.d("Check",uri);/*
            imgvw.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    // callIntent.setData(Uri.parse("tel:" +phone));
                    callIntent.setData(Uri.parse(uri));
                    // Log.d("Check",uri);
                    //Log.v("Check",uri);

                    // intent.setData(Uri.parse("tel:" + phone));

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pickDue) {
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

    //Make call
    public void makeACall(View view) {
        final int REQUEST_PHONE_CALL = 1;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:01680067688"));

        //checks for permission before placing the call

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){


            if (ActivityCompat.checkSelfPermission(PickUpActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                ActivityCompat.requestPermissions(PickUpActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }else {

                startActivity(callIntent);

            }
        }

    }

    // pop up menu for status options

    public void pickUpOptions(View view){
        PopupMenu popup = new PopupMenu(this,view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pickup_status_options);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Button btn_status = findViewById(R.id.button_status);
        switch (item.getItemId()){
            case R.id.confirm:
                Toast.makeText(this,"Pick Up Confirmed", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.green));
                btn_status.setText("Confirmed");
                return true;
            case R.id.callAgain:
                Toast.makeText(this,"Call Merchant later", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
                btn_status.setText("CallBack");
                return true;
            case R.id.rescheduled:
                Toast.makeText(this,"Pick Up Rescheduled", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.pfColor));
                btn_status.setText("R.S.");
                return true;
            case R.id.pendingReview:
                Toast.makeText(this,"Pick Up is pending review", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
                btn_status.setText("Pending");
                return true;
            case R.id.cancel:
                Toast.makeText(this,"Pick Up Cancelled", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                btn_status.setText("Cancelled");
                return true;
            default:  return false;

        }
    }
}
