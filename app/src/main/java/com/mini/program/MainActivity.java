package com.mini.program;

import static com.mini.program.miniTest.ForecaseWeather.v_apiresponse;
import static com.mini.program.miniTest.ForecaseWeather.v_statusApi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mini.program.helper.GlobalHelper;
import com.mini.program.miniTest.ForecaseWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    GlobalHelper        globalHelper = new GlobalHelper(this);
    ForecaseWeather     forecaseWeather = new ForecaseWeather(this);
    ProgressDialog      pDialog;
    Handler             handler = new Handler();
    DecimalFormat       formatter, latlong;
    String              city = "";
    int                 stepProses;
    ConstraintLayout    cl_next;
    TextView            tv_foobar, tv_weather;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest1);

        pDialog     = new ProgressDialog(this);
        cl_next     = findViewById(R.id.Q1_CL_NEXT);
        tv_foobar   = findViewById(R.id.Q1_TV_OUTPUT1);
        tv_weather  = findViewById(R.id.Q1_TV_OUTPUT2);

        _setupProfileSystem();

    }

    private void _setupProfileSystem() {
        globalHelper.Profile("PORTRAIT");
        tv_foobar.setText(getFooBar());
        getForcast();
    }


    private void getForcast() {
        SimpleDateFormat sdf    = new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.US);
        List<String> listdata   = new ArrayList<>();
        StringBuilder forecast  = new StringBuilder();

        formatter   = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        latlong     = new DecimalFormat("#.####", new DecimalFormatSymbols(Locale.US));

        pDialog.setMessage("Harap tunggu sebentar...");
        pDialog.setCancelable(false);
        pDialog.show();
        stepProses = 0;
        forecaseWeather.loadGetAPI();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (stepProses == 0) {
                    if (v_statusApi.equals("JSONOK")) {
                        Date dateBuffer = null;
                        try {
                            JSONObject jsonBody = new JSONObject(v_apiresponse);
                            JSONObject getcity = jsonBody.getJSONObject("city");
                            Log.e("getcity: ", getcity.toString());
                            JSONObject getcoord = getcity.getJSONObject("coord");
                            double latitude = getcoord.getDouble("lat");
                            double longitude = getcoord.getDouble("lon");
                            city += getcity.getString("name") + ", " +
                                    getcity.getString("country") +
                                    "\nCoordinate\t: " + latlong.format(latitude) +
                                    ", " + latlong.format(longitude);

                            JSONArray getlist = jsonBody.getJSONArray("list");
                            if (getlist.length() != 0) {
                                for (int i = 0; i < getlist.length(); i++) {
                                    JSONObject listItem = getlist.getJSONObject(i);
                                    long datems = listItem.getLong("dt") * 1000; // Convert to milliseconds
                                    Date date = new Date(datems);
                                    String dateString = sdf.format(date);

                                    JSONObject mainObject = listItem.getJSONObject("main");
                                    double suhu = mainObject.getDouble("temp") - 273.15; // Kelvin to Celcius

                                    if (dateBuffer == null) dateBuffer = date; //agar tidak null
                                    if (!globalHelper.isSameDay(date, dateBuffer)) {
                                        String data = dateString + "\t: " + formatter.format(suhu) + " C";
                                        listdata.add(data);
                                        dateBuffer = date;
                                    }
                                }

                                if (listdata.size() != 0) {
                                    for (int i = 0; i < listdata.size(); i++)
                                        forecast.append(listdata.get(i)).append("\n");

                                    city = city + "\n\nWeather Forecast\t:\n" + forecast;
                                    tv_weather.setText(city);
                                }

                                if (!tv_weather.getText().toString().isEmpty()) {
                                    stepProses = 1;
                                    pDialog.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (v_statusApi.equals("ERROR")) stepProses = 1;
                } else if (stepProses == 1) handler.removeCallbacks(this);
            }
        }, 100);
    }

    private String getFooBar() {
        String[] output = new String[100];
        for (int i = 1; i <= 100; i++) {
            int index = 100 - i;
            if (isPrime(i))
                output[index] = "";
            else if (i % 3 == 0 && i % 5 == 0)
                output[index] = "FooBar";
            else if (i % 3 == 0)
                output[index] = "Foo";
            else if (i % 5 == 0)
                output[index] = "Bar";
            else
                output[index] = Integer.toString(i);
        }

        StringBuilder foobar = new StringBuilder();
        for (String s : output) if (!s.isEmpty()) {
            foobar.append(s).append(" ");
        }

        return foobar.toString();
    }

    private static boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++)
            if (num % i == 0) return false;
        return true;
    }
}