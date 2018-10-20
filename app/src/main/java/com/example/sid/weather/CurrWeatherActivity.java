package com.example.sid.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class CurrWeatherActivity extends AppCompatActivity implements LocationListener {
    TextView t_title,t_c,t_f,t_city,t_state,t_country,t_lat,t_lon,t_wind,t_pressure,t_precip,t_humidity,t_cloud;
    String s_title,s_city,s_state,s_country,s_url;
    double s_lat,s_lon,s_c,s_f,s_wind,s_pressure,s_precip;
    int s_humidity,s_cloud;
    LocationManager locationManager;
    Double lan, lon;
    String key ="eacb25699779491c891171420181910";
    ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_weather);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        prog = findViewById(R.id.pro);
        t_title = findViewById(R.id.title);
        t_c = findViewById(R.id.cel);
        t_f = findViewById(R.id.fer);
        t_city = findViewById(R.id.vcity);
        t_state = findViewById(R.id.vstate);
        t_country = findViewById(R.id.vcountry);
        t_state = findViewById(R.id.vstate);
        t_lat = findViewById(R.id.vlat);
        t_lon = findViewById(R.id.vlon);
        t_wind = findViewById(R.id.vwind);
        t_pressure = findViewById(R.id.vpressure);
        t_precip = findViewById(R.id.vprecip);
        t_humidity = findViewById(R.id.vhumidity);
        t_cloud = findViewById(R.id.vcloud);
        prog.setVisibility(View.VISIBLE);
        getLocation();

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lan=location.getLatitude();
        lon=location.getLongitude();
        getWeather(lan,lon);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(CurrWeatherActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

        getLocation();
    }
    void getWeather(double lat ,double lant)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://api.apixu.com/v1/current.json?key="+key+"&q="+lat+","+lant;


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject locatn = response.getJSONObject("location");
                            s_city = locatn.getString("name");
                            s_state = locatn.getString("region");
                            s_country = locatn.getString("country");
                            s_lat = locatn.getDouble("lat");
                            s_lon = locatn.getDouble("lon");
                            JSONObject current = response.getJSONObject("current");
                            s_c = current.getDouble("temp_c");
                            s_f = current.getDouble("temp_f");
                            JSONObject condition = current.getJSONObject("condition");
                            s_title = condition.getString("text");
                            s_wind = current.getDouble("wind_kph");
                            s_pressure = current.getDouble("pressure_in");
                            s_precip = current.getDouble("precip_in");
                            s_humidity = current.getInt("humidity");
                            s_cloud = current.getInt("cloud");
                            t_title.setText(s_title);
                            t_c.setText(Double.toString(s_c));
                            t_f.setText(Double.toString(s_f));
                            t_city.setText(s_city);
                            t_state.setText(s_state);
                            t_country.setText(s_country);
                            t_lat.setText(Double.toString(s_lat));
                            t_lon.setText(Double.toString(s_lon));
                            t_wind.setText(Double.toString(s_wind));
                            t_pressure.setText(Double.toString(s_pressure));
                            t_precip.setText(Double.toString(s_precip));
                            t_humidity.setText(Integer.toString(s_humidity));
                            t_cloud.setText(Integer.toString(s_cloud));
                            prog.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(CurrWeatherActivity.this,"Check internet connection",Toast.LENGTH_SHORT).show();
                        CurrWeatherActivity.this.finish();
                    }
                }
        );


        queue.add(getRequest);

    }



}