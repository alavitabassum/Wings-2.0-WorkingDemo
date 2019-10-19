package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout.DeliverySummary_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryOfficerLandingPageDataLoad extends AppCompatActivity {
    private Activity activity;
    private Context context;

    public DeliveryOfficerLandingPageDataLoad(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

  /*  DeliveryOfficerLandingPageDataLoad(String username) {
        this.username = username;
    }*/
    /*SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
*/

    private RequestQueue requestQueue;
    private List<DeliverySummary_Model> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;

    public static final String GET_DELIVERY_SUMMARY = "http://paperflybd.com/deliveryAppLandingPage.php";

    BarcodeDbHelper db = new BarcodeDbHelper(context);
    //db.getWritableDatabase();

    private class GetLatestData extends AsyncTask<String, String, Void> {

        private String username;
        private Context context;
        public GetLatestData(String username, Context context) {
            this.username = username;
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            try {
               // context.getData

            } catch (Exception e){

            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... params) {
            try { loadDeliverySummary(username);
            } catch (Exception e) {
                //return "hello";
            }
            return null;
        }
    }


    private void loadDeliverySummary(final String user){
       /* progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_DELIVERY_SUMMARY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //db.getWritableDatabase();
                //SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                //db.deleteSummeryList(sqLiteDatabase);
                //progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                      /*  DeliverySummary_Model DeliverySummary = new DeliverySummary_Model(
                                o.getString("username"),
                                o.getString("unpicked"),
                                o.getString("withoutStatus"),
                                o.getString("onHold"),
                                o.getString("cash"),
                                o.getString("returnRequest"),
                                o.getString("returnList"),
                                o.getString("reAttempt"),
                                o.getString("returnRecv"),
                                o.getString("cashRecv"),
                                NAME_NOT_SYNCED_WITH_SERVER
                        );*/

                        db.insert_delivery_summary_count(
                                o.getString("username"),
                                o.getString("unpicked"),
                                o.getString("withoutStatus"),
                                o.getString("onHold"),
                                o.getString("cash"),
                                o.getString("returnRequest"),
                                o.getString("returnList"),
                                o.getString("reAttempt"),
                                o.getString("returnRecv"),
                                o.getString("cashRecv"),
                                NAME_NOT_SYNCED_WITH_SERVER
                        );
//                            summaries.add(todaySummary);
                    }

                    //getData(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // progress.dismiss();
                        Toast.makeText(activity.getApplicationContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void checkForChange(String username, Context context)
    {
        new GetLatestData(username, context).execute();
    }
}
