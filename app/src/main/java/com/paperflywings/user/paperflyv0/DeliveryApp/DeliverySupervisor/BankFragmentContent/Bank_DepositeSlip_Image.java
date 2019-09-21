package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisor.SERIAL_NO;

public class Bank_DepositeSlip_Image extends AppCompatActivity {

    ImageView image;
    private ProgressDialog progress;
    public static final String LOAD_URL = "http://paperflybd.com/DeliverySupervisorAPI.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon_sup_page);

        Intent intent = getIntent();
        String serialNo = intent.getStringExtra(SERIAL_NO);

        image = findViewById(R.id.imageInLarge) ;

        loadImageUrl(serialNo);
    }

    private void loadImageUrl(final String serialNo)
    {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOAD_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                       try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getImage");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);

                                String statusCode = o.getString("responseCode");

                                if(statusCode.equals("200")){
                                    String url = o.getString("imagePath");

                                    Picasso.with(getApplicationContext())
                                            .load(url)
                                            .fit()
                                            .into(image);

                                } else if(statusCode.equals("404")){
                                    Toast.makeText(Bank_DepositeSlip_Image.this, o.getString("unsuccess"), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            //swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        //swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("serialNo",serialNo);
                params1.put("flagreq","load_image_url");
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
