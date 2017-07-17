package com.example.enchanter19.otptrail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

public class checkotp extends AppCompatActivity implements OTPListener {
    EditText editText;
    Button button;
    String sotp,snumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkotp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        OtpReader.bind(this,"TSNEHA");
        editText=(EditText)findViewById(R.id.match);
        button=(Button)findViewById(R.id.verify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sotp=editText.getText().toString();
                snumber=getIntent().getStringExtra("mobile");



                logininto(snumber,sotp);
            }
        });




    }

    public void logininto(final String sphone1,final String sotp) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, globalurl.URL_VERIFYOTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");
                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("user_det");
                        String uname1 = users.getString("mobilenumber");
                        String uotp = users.getString("password");


                          Toast.makeText(getApplicationContext(),"otp have been verified",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Server Busy",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("usernumber", sphone1);
                insert.put("otp", sotp);


                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    @Override
    public void otpReceived(String messageText) {
        editText.setText(messageText);
    }
}
