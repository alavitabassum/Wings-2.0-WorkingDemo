package com.example.user.paperflyv0;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;

public class MyPickupList_Executive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, pickuplistForExecutiveAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    public static final String MERCHANT_NAME = "Merchant Name";
    public static final String MERCHANT_ID = "MerchantID";
    private static final String URL_DATA = "";
    private static final int REQUEST_CAMERA = 1;
    private ProgressDialog progress;
    private pickuplistForExecutiveAdapter pickuplistForExecutiveAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    private List<PickupList_Model_For_Executive> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_my_pickups__executive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();

        // Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

        recyclerView_pul = (RecyclerView) findViewById(R.id.recycler_view_mylist);

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dd = String.valueOf(date);

        getData(user);
        swipeRefreshLayout.setRefreshing(true);
//        list.clear();
        loadRecyclerView(user);

//        public String getBackupFolderName() {
//            Date date = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.hhmmss");
//            return sdf.format(date);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.executive_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);

        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.KITKAT)
        {
            if(checkPermission())
            {
//                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }


    /**
     * This method is to fetch all user records from SQLite
     */
    private void getData(final String user) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        ;

        new AsyncTask<String, Void , Void>() {
            @Override
            protected Void doInBackground(String... params) {
                list.clear();
                list.addAll(db.getAllData(user));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pickuplistForExecutiveAdapter = new pickuplistForExecutiveAdapter(list,getApplicationContext());
                recyclerView_pul.setAdapter(pickuplistForExecutiveAdapter);
                pickuplistForExecutiveAdapter.setOnItemClickListener(MyPickupList_Executive.this);
                pickuplistForExecutiveAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void loadRecyclerView(final String user)
    {
//        boolean check;
//          list.clear();
          final Date date = new Date();
          StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.117/new/showassign.php",
           new Response.Listener<String>()
           {
            @Override
            public void onResponse(String response) {
                //                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");

                    for(int i =0;i<array.length();i++)
                    {

                        JSONObject o = array.getJSONObject(i);
                        db.insert_my_assigned_pickups(
                                o.getString("executive_name"),
                                o.getString("order_count"),
                                o.getString("merchant_code"),
                                o.getString("assigned_by"),
                                o.getString("created_at"),
                                o.getString("updated_by"),
                                o.getString("updated_at"),
                                o.getString("scan_count"),
                                o.getString("phone_no"),
                                o.getString("picked_qty"),
                                o.getString("merchant_name"),
                                o.getString("complete_status")
                                , NAME_NOT_SYNCED_WITH_SERVER );

                    }
                     getData(user);
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Serve not connected loadrecylerview" +error ,Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("executive_name",user);
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    // Function for camera permission
    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    // Request for camera permission
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    // Actions on camera permission grant result
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    // Camera permission ok or cancel
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MyPickupList_Executive.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Change status code section (start)
    public void changeStatus(View view){
        final CharSequence[] status_options = {"Cancel","Pending"};
        final String[] selection = new String[1];
        vwParentRow = (android.widget.RelativeLayout) view.getParent();
        final Button btn_status  = (Button)vwParentRow.getChildAt(10);
        AlertDialog.Builder eBuilder = new AlertDialog.Builder(MyPickupList_Executive.this);
        eBuilder.setTitle("Change Pickup Status").setSingleChoiceItems(status_options, -1 , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){

                    case 0:
                        selection[0] = (String) status_options[0];
                        break;

                    case 1:
                        selection[0] = (String) status_options[1];
                        btn_status.setTextColor(Color.BLACK);
                        btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.btn_yellow));
                        btn_status.setText("Pending");
                        dialogInterface.dismiss();
                        break;
                }

            }
        }).setCancelable(false).setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                             final CharSequence[] cancel_options = {"Merchant unavailable","Fautly order","Others"};
                             final String[] cancelSelection = new String[1];

                             AlertDialog.Builder cancelreasonBuilder = new AlertDialog.Builder(MyPickupList_Executive.this);
                           if (selection[0] == status_options[0]){

                                    cancelreasonBuilder.setTitle("Cancellation Reason").setSingleChoiceItems(cancel_options, -1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface1, int position) {
                                            switch (position){
                                                case 0:
                                                    cancelSelection[0] = (String) cancel_options[0];
                                                    break;
                                                case 1:
                                                    cancelSelection[0] = (String) cancel_options[1];
                                                    break;
                                                case 2:
                                                    cancelSelection[0] = (String) cancel_options[2];
                                                    break;

                                            }

                                            btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                                            btn_status.setTextColor(Color.WHITE);
                                            btn_status.setText("cancel");
                                        }
                                    }).setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface1, int which) {
                                            if ( cancelSelection[0] == cancel_options[0] || cancelSelection[0] == cancel_options[1] ||cancelSelection[0] == cancel_options[2]){
                                                Toast.makeText(MyPickupList_Executive.this, "Pickup Cancelled", Toast.LENGTH_SHORT).show();
                                                dialogInterface1.dismiss();
                                            }else{
                                                ((AlertDialog)dialogInterface1).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                            }
                                        }
                                    });

                           }else if (selection[0] == status_options[1]){

                                    //    dialogInterface.dismiss();
                               Toast.makeText(MyPickupList_Executive.this, "Pickup Pending", Toast.LENGTH_SHORT).show();

                           }else if(selection[0] != status_options[1] && selection[0] != status_options[0]){

                               ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                               Toast.makeText(MyPickupList_Executive.this, "Pickup Pending", Toast.LENGTH_SHORT).show();

                           }

                AlertDialog rDialog = cancelreasonBuilder.create();
                rDialog.show();


            }
        });
        vwParentRow.refreshDrawableState();
        AlertDialog eDialog = eBuilder.create();
        eDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Intent homeIntent = new Intent(MyPickupList_Executive.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        } else {
            super.onBackPressed();
            Intent homeIntent = new Intent(MyPickupList_Executive.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
try{  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        pickuplistForExecutiveAdapter.getFilter().filter(newText);
        return false;
    }
});
}catch (Exception e)
{
    e.printStackTrace();
    Intent intent_stay = new Intent(MyPickupList_Executive.this,MyPickupList_Executive.class);
    Toast.makeText(this, "Page Loading...", Toast.LENGTH_SHORT).show();
    startActivity(intent_stay);
}
        return true;
    }
//testing
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(MyPickupList_Executive.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_pickup_sum) {
            Intent pickupIntent = new Intent(MyPickupList_Executive.this,
                    PickupsToday_Executive.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_exe_pickup) {
            Intent assignIntent = new Intent(MyPickupList_Executive.this,
                    MyPickupList_Executive.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_pickStatus) {
            Intent historyIntent = new Intent(MyPickupList_Executive.this,
                    PickupStatus_Executive.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            //Getting out sharedpreferences
                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                            //Putting blank value to email
                            editor.putString(Config.EMAIL_SHARED_PREF, "");

                            //Saving the sharedpreferences
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(MyPickupList_Executive.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            //Showing the alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent scanIntent = new Intent(MyPickupList_Executive.this, ScanningScreen.class);

        PickupList_Model_For_Executive clickedItem = list.get(position);

        scanIntent.putExtra(MERCHANT_NAME, clickedItem.getMerchant_name());
        scanIntent.putExtra(MERCHANT_ID, clickedItem.getMerchant_id());
//        scanIntent.putExtra(ITEM_POSITION, String.valueOf(position));

        startActivity(scanIntent);
    }

    @Override
    public void onItemClick_view(View view2, int position2) {

    }

    @Override
    public void onRefresh() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dd = String.valueOf(date);
        getData(username);
        loadRecyclerView(username);
    }

}
