package com.example.user.paperflyv0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignPickup_Manager extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AssignExecutiveAdapter.OnItemClickListener{

    String[] executive_num_list;
    public static final String MERCHANT_NAME = "Merchant Name";
    private String EXECUTIVE_URL = "http://paperflybd.com/executiveList.php";
    public static final String INSERT_URL = "http://192.168.0.117/new/insertassign.php";
    //private String MERCHANT_URL= "http://192.168.0.117/new/merchantlistt.php";
    private String MERCHANT_URL = "http://paperflybd.com/unassignedAPI.php";
    private String ALL_MERCHANT_URL = "http://paperflybd.com/merchantAPI.php";
    private AssignExecutiveAdapter assignExecutiveAdapter;
    List<AssignManager_ExecutiveList> executiveLists;
    List<AssignManager_Model> assignManager_modelList;
    Database database;

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;



    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FloatingActionMenu fabmenu;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;

    //    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_pickup__manager);
        database = new Database(getApplicationContext());
        database.getWritableDatabase();
        executiveLists = new ArrayList<>();
        assignManager_modelList = new ArrayList<>();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        String user = username.toString();

        //recycler with cardview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_merchant);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        getallmerchant();
        //getallexecutives();

        loadmerchantlist(user);
        loadexecutivelist(user);
        loadallmerchantlist(user);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again

            }
        };


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabmenu = (FloatingActionMenu) findViewById(R.id.menu);
         fab1 = (FloatingActionButton) findViewById(R.id.menu_item1);
    /*     fab2 = (FloatingActionButton) findViewById(R.id.menu_item2);*/

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "Coming soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intentorder = new Intent(AssignPickup_Manager.this,
                        NewOrder.class);
                startActivity(intentorder);
            }
        });

       /* fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentorderupdate = new Intent(AssignPickup_Manager.this,
                        NewOrder.class);
                startActivity(intentorderupdate);
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.manager_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Load executive from api
    private void loadexecutivelist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, EXECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("executivelist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                database.addexecutivelist(o.getString("userName"), o.getString("empCode"));
                            }
                            getallexecutives();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest1);
    }









//
//
//
//
//    //Get Executive List from sqlite
//    private void getallexecutives() {
//        try {
//
//            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
//            Cursor c = database.get_executivelist(sqLiteDatabase);
//            while (c.moveToNext()) {
//                String empName = c.getString(0);
//                String empCode = c.getString(1);
//                AssignManager_ExecutiveList assignManager_executiveList = new AssignManager_ExecutiveList(empName, empCode);
//                executiveLists.add(assignManager_executiveList);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getallexecutives() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                executiveLists.clear();
                executiveLists.addAll(database.getAllExecutiveList());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }.execute();
    }













    //Merchant List API hit
    private void loadmerchantlist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, MERCHANT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("unAssignedlist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                database.addmerchantlist(o.getString("merchantName"), o.getString("merchantCode"),o.getInt("cnt"));
                            }
                            getallmerchant();

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest1);
    }

    // merchant List generation from sqlite
//    private void getallmerchant() {
//        try {
//
//            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
//            Cursor c = database.get_merchantlist(sqLiteDatabase);
//
//            while (c.moveToNext()) {
//                String merchantName = c.getString(0);
//                String merchantCode = c.getString(1);
//                int totalcount = c.getInt(2);
//                AssignManager_Model todaySummary = new AssignManager_Model(merchantName, merchantCode,totalcount);
//                assignManager_modelList.add(todaySummary);
//            }
//
//            assignExecutiveAdapter = new AssignExecutiveAdapter(assignManager_modelList, getApplicationContext());
//            recyclerView.setAdapter(assignExecutiveAdapter);
//            assignExecutiveAdapter.setOnItemClickListener(AssignPickup_Manager.this);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * This method is to fetch all merchant list from SQLite
     */
    private void getallmerchant() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                assignManager_modelList.clear();
                assignManager_modelList.addAll(database.getAllMerchantList());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                assignExecutiveAdapter = new AssignExecutiveAdapter(assignManager_modelList, getApplicationContext());
                recyclerView.setAdapter(assignExecutiveAdapter);
                assignExecutiveAdapter.setOnItemClickListener(AssignPickup_Manager.this);
                assignExecutiveAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


    //Merchant List API hit
    private void loadallmerchantlist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, ALL_MERCHANT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("merchantlist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                database.addallmerchantlist(o.getString("merchantName"), o.getString("merchantCode"));
                            }

                            } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest1);
    }

    private void assignexecutivetosqlite(final String ex_name, final String empcode, final String order_count, final String merchant_code, final String user, final String currentDateTimeString,final int status) {

            database.assignexecutive(ex_name,empcode,order_count, merchant_code, user, currentDateTimeString,status);
            //final int total_assign = database.getTotalOfAmount(merchant_code);
            //final String strI = String.valueOf(total_assign);
            //database.update_row(strI, merchant_code);

    }
        //For assigning executive API into mysql
    private void assignexecutive(final String ex_name, final String empcode, final String order_count, final String merchant_code, final String user, final String currentDateTimeString,final String m_name) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                assignexecutivetosqlite(ex_name,empcode,order_count,merchant_code,user,currentDateTimeString,NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                assignexecutivetosqlite(ex_name,empcode,order_count,merchant_code,user,currentDateTimeString, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        assignexecutivetosqlite(ex_name,empcode,order_count,merchant_code,user,currentDateTimeString, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("executive_name", ex_name);
                params.put("executive_code", empcode);
                params.put("order_count", order_count);
                params.put("merchant_code", merchant_code);
                params.put("assigned_by", user);
                params.put("created_at", currentDateTimeString);
                params.put("merchant_name", m_name);
               /* database.assignexecutive(ex_name,empcode,order_count, merchant_code, user, currentDateTimeString);
                final int total_assign = database.getTotalOfAmount(merchant_code);
                final String strI = String.valueOf(total_assign);
                database.update_row(strI, merchant_code);*/
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

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
        getMenuInflater().inflate(R.menu.menu_item, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
try{
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            assignExecutiveAdapter.getFilter().filter(newText);
            return false;
        }
    });
}catch (Exception e)
{
    e.printStackTrace();
    Intent intent_stay = new Intent(AssignPickup_Manager.this,AssignPickup_Manager.class);
    Toast.makeText(this, "Page Loading...", Toast.LENGTH_SHORT).show();
    startActivity(intent_stay);
}

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

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(AssignPickup_Manager.this,
                    ManagerCardMenu.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(AssignPickup_Manager.this,
                    PickupsToday_Manager.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(AssignPickup_Manager.this,
                    AssignPickup_Manager.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_pickCompleted) {
            Intent historyIntent = new Intent(AssignPickup_Manager.this,
                    PickupHistory_Manager.class);
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
                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                            //Putting blank value to email
                            editor.putString(Config.EMAIL_SHARED_PREF, "");

                            //Saving the sharedpreferences
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(AssignPickup_Manager.this, LoginActivity.class);
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
    public void onItemClick(View view, int position) {

        final AssignManager_Model clickeditem = assignManager_modelList.get(position);

       AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(AssignPickup_Manager.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        spinnerBuilder.setTitle("Select executive and assign number.");


        final TextView dialog_mName = mView.findViewById(R.id.dialog_m_name);
        final AutoCompleteTextView mAutoComplete = mView.findViewById(R.id.auto_exe);
        final EditText et1 = mView.findViewById(R.id.spinner1num);
        dialog_mName.setText(clickeditem.getM_names());
        final String merchant_code = clickeditem.getM_address();
        final String m_name = clickeditem.getM_names();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String user = username.toString();

        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        List<String> lables = new ArrayList<String>();

        for (int z = 0; z < executiveLists.size(); z++) {
            lables.add(executiveLists.get(z).getExecutive_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignPickup_Manager.this,
                android.R.layout.simple_list_item_1, lables);

        mAutoComplete.setAdapter(adapter);

        spinnerBuilder.setPositiveButton("Assign", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i1) {
                 String empname = mAutoComplete.getText().toString();
                 final String empcode = database.getSelectedEmployeeCode(empname);
                 assignexecutive(mAutoComplete.getText().toString(),empcode, et1.getText().toString(), merchant_code, user, currentDateTimeString,m_name);

                if (mAutoComplete.getText().toString().isEmpty() || mAutoComplete.getText().toString().equals(null)) {
                    Toast.makeText(AssignPickup_Manager.this, mAutoComplete.getText().toString()
                                    + "(" + et1.getText().toString() + ")",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

            }
        });

        spinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i1) {
                dialog.dismiss();
            }
        });
        spinnerBuilder.setCancelable(false);
        // vwParentRow2.refreshDrawableState();
        spinnerBuilder.setView(mView);
        AlertDialog dialog2 = spinnerBuilder.create();
        dialog2.show();


    }

    @Override
    public void onItemClick_view(View view2, int position2) {

        final AssignManager_Model clickeditem2 = assignManager_modelList.get(position2);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String merchantname = clickeditem2.getM_names();
        String merchantcode = clickeditem2.getM_address();
        Intent intent = new Intent(AssignPickup_Manager.this, ViewAssigns.class);
        intent.putExtra("MERCHANTNAME", merchantname);
        intent.putExtra("MERCHANTCODE",merchantcode);
        startActivity(intent);

    }


    @Override
    public void onItemClick_update(View view3, int position3) {
        final AssignManager_Model clickeditem3 = assignManager_modelList.get(position3);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String merchantname = clickeditem3.getM_names();
        String merchantcode = clickeditem3.getM_address();
        Intent intent = new Intent(AssignPickup_Manager.this, UpdateAssigns.class);
        intent.putExtra("MERCHANTNAME", merchantname);
        intent.putExtra("MERCHANTCODE",merchantcode);
        startActivity(intent);
    }
}