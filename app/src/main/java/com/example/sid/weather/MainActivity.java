package com.example.sid.weather;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button cur, fin;
    EditText ser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cur = findViewById(R.id.curbut);
        fin = findViewById(R.id.finbut);
        ser = findViewById(R.id.sertext);
        if (!isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        } else {


        cur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CurrWeatherActivity.class);
                startActivity(i);
            }
        });
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val = ser.getText().toString();
                if (val.isEmpty() || val.equals("Lat,Lon/City") || val.equals(" ")) {
                    Toast.makeText(MainActivity.this, "Please enter city name or zip code", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, FinWeatherActivity.class);
                    i.putExtra("text", ser.getText().toString());
                    startActivity(i);
                }
            }
        });
    }

    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
