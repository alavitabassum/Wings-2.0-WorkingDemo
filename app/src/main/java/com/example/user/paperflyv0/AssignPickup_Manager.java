package com.example.user.paperflyv0;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class AssignPickup_Manager extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    String[] executive_num_list;
    private String URL_DATA = "http://192.168.0.130/new/executivelist.php";
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
        final String[] selectionNum = new String[1];
        AlertDialog.Builder builder_assign = new AlertDialog.Builder(AssignPickup_Manager.this);
        builder_assign.setTitle("How many executives do you want to assign?");
        builder_assign.setSingleChoiceItems(executive_num_list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                //selected.setText(executive_num_list[i]);
                switch(i){

                    case 0:
                        selectionNum[0] = executive_num_list[0];
                        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        break;
                    case 1:
                        selectionNum[0] = executive_num_list[1];
                        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        break;
                    case 2:
                        selectionNum[0] = executive_num_list[2];
                        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        break;
                }
            }
        }).setCancelable(false).setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                List<String> lables = new ArrayList<String>();
                for (int z = 0; z < executiveLists.size(); z++) {
                    lables.add(executiveLists.get(z).getExecutive_name());
                }

                    AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(AssignPickup_Manager.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_spinner,null);
                    spinnerBuilder.setTitle("Select executive and assign number.");
                    final Spinner mSpinner1 = mView.findViewById(R.id.spinner1);
                    final EditText et1 = mView.findViewById(R.id.spinner1num);
                    final Spinner mSpinner2 = mView.findViewById(R.id.spinner2);
                    final EditText et2 = mView.findViewById(R.id.spinner2num);
                    final Spinner mSpinner3 = mView.findViewById(R.id.spinner3);
                    final EditText et3 = mView.findViewById(R.id.spinner3num);
                    final RelativeLayout r2 =mView.findViewById(R.id.spinner_sec_2);
                    final RelativeLayout r3 =mView.findViewById(R.id.spinner_sec_3);
                    ArrayAdapter<String>  adapter = new ArrayAdapter<String>(AssignPickup_Manager.this,
                            android.R.layout.simple_spinner_item,
                            lables);


                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner1.setAdapter(adapter);
                    mSpinner2.setAdapter(adapter);
                    mSpinner3.setAdapter(adapter);

                if (selectionNum[0] == executive_num_list[0]){
                    r2.setVisibility(View.GONE);
                    r3.setVisibility(View.GONE);
                }else if(selectionNum[0] == executive_num_list[1]){
                    r3.setVisibility(View.GONE);
                }

                    spinnerBuilder.setPositiveButton("Assign", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i1) {
                            if (selectionNum[0] == executive_num_list[0]){
                                if (!mSpinner1.getSelectedItem().toString().equalsIgnoreCase("Choose executive…")){
                                    Toast.makeText(AssignPickup_Manager.this, mSpinner1.getSelectedItem().toString()
                                          +"("+et1.getText().toString() +")",
                                            Toast.LENGTH_SHORT).show();
                                    selection1.setText(mSpinner1.getSelectedItem().toString());
                                    selection1.setTextColor(getResources().getColor(R.color.pfColor));
                                    assignedNum.setText(et1.getText().toString());
                                    selection2.setVisibility(View.GONE);
                                    selection3.setVisibility(View.GONE);
                                    dialog.dismiss();

                                }
                            }else if(selectionNum[0] == executive_num_list[1]){
                                if (!mSpinner1.getSelectedItem().toString().equalsIgnoreCase("Choose executive…") && !mSpinner2.getSelectedItem().toString().equalsIgnoreCase("Choose executive…")){
                                    Toast.makeText(AssignPickup_Manager.this,mSpinner1.getSelectedItem().toString() +"("+et1.getText().toString() +")"+"\n"+ mSpinner2.getSelectedItem().toString() +"("+et2.getText().toString() +")",
                                            Toast.LENGTH_SHORT).show();
                                    selection1.setText(mSpinner1.getSelectedItem().toString());
                                    selection2.setText(mSpinner2.getSelectedItem().toString());
                                    selection1.setTextColor(getResources().getColor(R.color.pfColor));
                                    selection2.setTextColor(getResources().getColor(R.color.pfColor));
                                    assignedNum.setText(et1.getText().toString());
                                    selection3.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    selection1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selection1.setBackgroundColor(getResources().getColor(R.color.light_grey));
                                            selection2.setBackgroundColor(Color.WHITE);
                                            assignedNum.setText(et1.getText().toString());
                                        }
                                    });

                                    selection2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selection2.setBackgroundColor(getResources().getColor(R.color.light_grey));
                                            selection1.setBackgroundColor(Color.WHITE);
                                            assignedNum.setText(et2.getText().toString());
                                        }
                                    });
                                }
                            }else if (selectionNum[0] == executive_num_list[2]){
                                if (!mSpinner1.getSelectedItem().toString().equalsIgnoreCase("Choose executive…") && !mSpinner2.getSelectedItem().toString().equalsIgnoreCase("Choose executive…") && !mSpinner3.getSelectedItem().toString().equalsIgnoreCase("Choose executive…")){
                                    Toast.makeText(AssignPickup_Manager.this,mSpinner1.getSelectedItem().toString()+"("+et1.getText().toString() +")"+"\n"+ mSpinner2.getSelectedItem().toString()+"("+et2.getText().toString() +")"+"\n"+  mSpinner3.getSelectedItem().toString()+"("+et3.getText().toString() +")",
                                            Toast.LENGTH_SHORT).show();
                                    selection1.setText(mSpinner1.getSelectedItem().toString());
                                    selection2.setText(mSpinner2.getSelectedItem().toString());
                                    selection3.setText(mSpinner3.getSelectedItem().toString());
                                    selection1.setTextColor(getResources().getColor(R.color.pfColor));
                                    selection2.setTextColor(getResources().getColor(R.color.pfColor));
                                    selection3.setTextColor(getResources().getColor(R.color.pfColor));
                                    assignedNum.setText(et1.getText().toString());
                                    dialog.dismiss();
                                    selection1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selection1.setBackgroundColor(getResources().getColor(R.color.light_grey));
                                            selection2.setBackgroundColor(Color.WHITE);
                                            selection3.setBackgroundColor(Color.WHITE);
                                            assignedNum.setText(et1.getText().toString());
                                        }
                                    });

                                    selection2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selection2.setBackgroundColor(getResources().getColor(R.color.light_grey));
                                            selection1.setBackgroundColor(Color.WHITE);
                                            selection3.setBackgroundColor(Color.WHITE);
                                            assignedNum.setText(et2.getText().toString());
                                        }
                                    });
                                    selection3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selection3.setBackgroundColor(getResources().getColor(R.color.light_grey));
                                            selection1.setBackgroundColor(Color.WHITE);
                                            selection2.setBackgroundColor(Color.WHITE);
                                            assignedNum.setText(et3.getText().toString());
                                        }
                                    });

                                }
                            }


                        }
                    });
                    spinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i1) {
                            dialog.dismiss();
                        }
                    });

                    spinnerBuilder.setView(mView);
                    AlertDialog dialog2 = spinnerBuilder.create();
                    dialog2.show();


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        vwParentRow2.refreshDrawableState();
        AlertDialog Dialog_assign = builder_assign.create();
        Dialog_assign.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                if(selectionNum[0] != executive_num_list[0]|| selectionNum[0] != executive_num_list[1] || selectionNum[0] != executive_num_list[2])
                {
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

        Dialog_assign.show();


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
