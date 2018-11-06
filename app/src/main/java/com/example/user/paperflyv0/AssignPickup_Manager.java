package com.example.user.paperflyv0;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignPickup_Manager extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    String[] executive_num_list;
    private String URL_DATA = "http://192.168.0.130/new/executivelist.php";
    private String INSERT_URL = "http://192.168.0.130/new/insertassign.php";
    List<AssignManager_ExecutiveList> executiveLists;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    android.widget.RelativeLayout vwParentRow2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_pickup__manager);
        executiveLists = new ArrayList<>();

        //recycler with cardview

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_merchant);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadRecyclerView();
        adapter = new AssignExecutiveAdapter();
        recyclerView.setAdapter(adapter);

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

    private void loadRecyclerView()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("executives");
                    for(int i =0;i<array.length();i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        AssignManager_ExecutiveList assignManager_executiveList = new AssignManager_ExecutiveList(
                                o.getString("executive_name")
                        );
                        executiveLists.add(assignManager_executiveList);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Change status code section (start)
    public void assignExe(View view){
        vwParentRow2 = (android.widget.RelativeLayout) view.getParent();

        final TextView assignedNum = (TextView)vwParentRow2.getChildAt(6);
        final TextView CompleteNum = (TextView)vwParentRow2.getChildAt(7);
        final TextView DueNum = (TextView)vwParentRow2.getChildAt(8);
        final TextView selection1 = (TextView)vwParentRow2.getChildAt(9);
        final TextView selection2 = (TextView)vwParentRow2.getChildAt(10);
        final TextView selection3 = (TextView)vwParentRow2.getChildAt(11);
        executive_num_list = new String[]{"1","2","3"};


        AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(AssignPickup_Manager.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner,null);
        spinnerBuilder.setTitle("Select executive and assign number.");
        final Spinner mSpinner1 = mView.findViewById(R.id.spinner1);
        final EditText et1 = mView.findViewById(R.id.spinner1num);




        List<String> lables = new ArrayList<String>();

        for (int z = 0; z < executiveLists.size(); z++) {
            lables.add(executiveLists.get(z).getExecutive_name());
        }

        ArrayAdapter<String>  adapter = new ArrayAdapter<String>(AssignPickup_Manager.this,
                android.R.layout.simple_spinner_item,
                lables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(adapter);

        spinnerBuilder.setPositiveButton("Assign", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i1) {
                assignexecutive(mSpinner1.getSelectedItem().toString(),et1.getText().toString());

                if (!mSpinner1.getSelectedItem().toString().equalsIgnoreCase("Choose executive…")){
                    Toast.makeText(AssignPickup_Manager.this, mSpinner1.getSelectedItem().toString()
                                    +"("+et1.getText().toString() +")",
                            Toast.LENGTH_SHORT).show();
                    selection1.setText(mSpinner1.getSelectedItem().toString());
                    selection1.setTextColor(getResources().getColor(R.color.pfColor));
                    assignedNum.setText(et1.getText().toString());
                        /*selection2.setVisibility(View.GONE);
                        selection3.setVisibility(View.GONE);*/
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
        vwParentRow2.refreshDrawableState();
        spinnerBuilder.setView(mView);
        AlertDialog dialog2 = spinnerBuilder.create();
    /*    dialog2.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                if(mSpinner1.getSelectedItem().toString().equals("Choose executive…"))
                {
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });*/

        dialog2.show();


    }
    private void assignexecutive(final String ex_name, final String order_count) {


        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    //   Log.d("Error",error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("executive_name",ex_name);
                params.put("order_count",order_count);

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
        getMenuInflater().inflate(R.menu.pickups_today__manager, menu);
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
        }
        else if (id == R.id.nav_pickDue) {
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
            Intent loginIntent = new Intent(AssignPickup_Manager.this,
                    LoginActivity.class);
            startActivity(loginIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}