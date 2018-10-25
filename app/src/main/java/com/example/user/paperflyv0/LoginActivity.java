package com.example.user.paperflyv0;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button signin;
    EditText username,pass;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin = (Button) findViewById(R.id.sign_in);
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Welcome Activity
            Intent intent = new Intent(LoginActivity.this, Welcome.class);
            startActivity(intent);
        }
    }

    public void login()
    {
        //Getting values from edit texts
        final String user = username.getText().toString().trim();
        final String pas = pass.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "http://paperflybd.com/la.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.contains("failure")){
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                }
                else
                {
                try {
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                    editor.putString(Config.EMAIL_SHARED_PREF, user);

                    //Saving values to editor
                    editor.commit();

                    JSONArray arr = new JSONArray(response);
                    JSONObject jObj = arr.getJSONObject(0);
                    String userRole = jObj.getString("userRole");
                    if (userRole.contains("0")) {

                        startActivity(new Intent(getApplicationContext(),CardDemoActivity.class));
                    } else {

                        startActivity(new Intent(getApplicationContext(),Welcome.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",user);
                params.put("pass",pas);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

}
