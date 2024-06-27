package com.z8ten.wheatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private final String url="https://api.openweathermap.org/data/2.5/weather";
    private final String appid="4cd90a0be5038ed460fd4d5411672070";
    DecimalFormat df=new DecimalFormat("#.##");
    EditText ecity,ecode;
    Button get;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecity=findViewById(R.id.et_ecity);
        ecode=findViewById(R.id.et_ecode);
        get=findViewById(R.id.get);
        result=findViewById(R.id.tv_result);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempurl="";
                String city=ecity.getText().toString().trim();
                String country=ecode.getText().toString().trim();

                if(city.equals("")){
                    result.setText("Enter city ");
                }else{
                    if(!country.equals("")){
                        tempurl=url+"?q="+city+","+country+"&appid="+appid;
                    }
                    else{
                        tempurl=url+"?q="+city+"&appid="+appid;
                    }
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            String output="";
                            try {
                                JSONObject jsonResponse=new JSONObject(response);
                                JSONArray jsonArray=jsonResponse.getJSONArray("weather");
                                JSONObject jsonObjectWeather=jsonArray.getJSONObject(0);
                                String description=jsonObjectWeather.getString("description");
                                JSONObject jsonObjectmain=jsonResponse.getJSONObject("main");
                                double temp=jsonObjectmain.getDouble("temp")-273.15;
                                double feelsLike=jsonObjectmain.getDouble("feels_like")-273.15;
                                float pressure=jsonObjectmain.getInt("pressure");
                                int humidity=jsonObjectmain.getInt("humidity");

                                output+="Temp="+temp+"\n"+"feelsLike="+feelsLike+"\n"+"Pressure="+pressure+"\n"+"Humidity="+humidity;

                                result.setText(output);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error".toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }

            }
        });

    }
}