package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String URL;
    private double screenLatitude;
    private double screenLongitude;
    private TextView temperatureText;
    private TextView appDate;
    private TextView locationCoordinates;
    private TextView hour1;
    private TextView hour2;
    private TextView hour3;
    private TextView hour4;
    private TextView hour5;
    private TextView hourTemp1;
    private TextView hourTemp2;
    private TextView hourTemp3;
    private TextView hourTemp4;
    private TextView hourTemp5;
    private ImageView hourImage1;
    private ImageView hourImage2;
    private ImageView hourImage3;
    private ImageView hourImage4;
    private ImageView hourImage5;
    private TextView day1;
    private TextView day2;
    private TextView day3;
    private TextView day4;
    private TextView day5;
    private TextView dayTemp1;
    private TextView dayTemp2;
    private TextView dayTemp3;
    private TextView dayTemp4;
    private TextView dayTemp5;
    private ImageView dayImage1;
    private ImageView dayImage2;
    private ImageView dayImage3;
    private ImageView dayImage4;
    private ImageView dayImage5;
    private LinearLayout hourTemperature;
    private LinearLayout dayTemperature;
    private PhotoView nytImage;

    private HashMap<String, String> states = new HashMap<String, String>();

    private String[] dayList = new String[]{
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };
    private SearchView search;
    private Button mDisplayDate;
    private Button resetLocationDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (SearchView) findViewById(R.id.searchLocation);
        search.setQueryHint("Search any city in the world!");

        locationCoordinates = (TextView) findViewById(R.id.location);
        mDisplayDate = (Button) findViewById(R.id.date);
        appDate = (TextView) findViewById(R.id.appDate);
        resetLocationDate = (Button) findViewById(R.id.reset);

        hourTemperature = (LinearLayout) findViewById(R.id.hourTemperature);
        dayTemperature = (LinearLayout) findViewById(R.id.dayTemperature);
        nytImage = (PhotoView) findViewById(R.id.nytImage);
        nytImage.setVisibility(View.GONE);

        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","QC");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");

        appDate.setText("Today");

        resetLocationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentWeather();
            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = month + "/" + dayOfMonth + "/" + year;

                System.out.println(year + " " + month + " " + dayOfMonth);

                Date dateData = new Date(year - 1900, month - 1, dayOfMonth, 13, 0);
                long timestamp = dateData.getTime() / 1000;

                System.out.println(date);
                appDate.setText(date);
                changeTimeOfWeather(timestamp);

                hourTemperature.setVisibility(View.GONE);
                dayTemperature.setVisibility(View.GONE);
                nytImage.setVisibility(View.VISIBLE);


            }
        };

        setCurrentWeather();
        beginSearch();

    }

    public void changeTimeOfWeather(final long timestamp){

        System.out.println(timestamp);

        URL = "https://api.darksky.net/forecast/fd1dc62aeb4a000f926fa335263b82c4/" + screenLatitude + "," + screenLongitude + "," +
                timestamp;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(

                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(timestamp * 1000);
                            String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                            String month = ((int) calendar.get(Calendar.MONTH) + 1) + "";
                            String year = calendar.get(Calendar.YEAR) + "";


                            if (Integer.parseInt(day) < 10){
                                day = "0" + day;
                            }

                            if (Integer.parseInt(month) < 10){
                                month = "0" + month;
                            }

                            String imageURL = "https://static01.nyt.com/images/" + year + "/" + month + "/" + day + "/nytfrontpage/scan.jpg";

                            temperatureText.setText(Math.round(Double.parseDouble(response.getJSONObject("currently").getString("temperature"))) + "°");

                            String icon = response.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("icon");
                            icon = icon.replace("-", "_");

                            Glide.with(getApplicationContext())
                                .load(imageURL)
                                    .placeholder(getResources().getIdentifier(icon, "drawable", getPackageName()))
                                    .into(nytImage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );

        requestQueue.add(objectRequest);
    }

    public void setCurrentWeather(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println(1.2);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String cityName = "";
                Address address = null;

                try {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    address = addresses.get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                setWeather(address);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
            }, 10);
            return;
        }
        else{
            System.out.println(1);
            locationManager.requestLocationUpdates("gps", 10000, 500, locationListener);
        }
    }

    public void beginSearch(){

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = search.getQuery().toString();
                List<Address> locationSearchList = null;

                if (location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    System.out.println(geocoder);

                    try {
                        locationSearchList = geocoder.getFromLocationName(location, 6);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = locationSearchList.get(0);
                    System.out.println(address.getLatitude());
                    setWeather(address);

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates("gps", 10000, 500, locationListener);
        }
    }

    public String getHourAndLabel(int hour){

        hour = hour % 24;
        String label = "";

        if (hour > 11){
            label = "PM";
        }
        else{
            label = "AM";
        }
        if (hour == 0){
            hour = 12;
        }
        else if (hour == 12){
            hour = 12;
        }
        else{
            hour = hour % 12;
        }
        return hour + label;
    }

    public void setWeather(Address address){

        appDate.setText("Today");
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();
        screenLatitude = latitude;
        screenLongitude = longitude;
        String cityName = address.getLocality();
        String state = address.getAdminArea();
        String totalLocation = "";

        if (state != null){
            totalLocation += cityName + ", " + states.get(state);
        }

        else{
            totalLocation = cityName;
        }

        hourTemperature.setVisibility(View.VISIBLE);
        dayTemperature.setVisibility(View.VISIBLE);
        nytImage.setVisibility(View.GONE);

        URL = "https://api.darksky.net/forecast/fd1dc62aeb4a000f926fa335263b82c4/" + latitude + "," + longitude;
        locationCoordinates.setText(totalLocation);

        temperatureText = (TextView) findViewById(R.id.temperature);
        hour1 = (TextView) findViewById(R.id.hour1);
        hour2 = (TextView) findViewById(R.id.hour2);
        hour3 = (TextView) findViewById(R.id.hour3);
        hour4 = (TextView) findViewById(R.id.hour4);
        hour5 = (TextView) findViewById(R.id.hour5);
        hourTemp1 = (TextView) findViewById(R.id.hourtemp1);
        hourTemp2 = (TextView) findViewById(R.id.hourtemp2);
        hourTemp3 = (TextView) findViewById(R.id.hourtemp3);
        hourTemp4 = (TextView) findViewById(R.id.hourtemp4);
        hourTemp5 = (TextView) findViewById(R.id.hourtemp5);
        hourImage1 = (ImageView) findViewById(R.id.hourImage1);
        hourImage2 = (ImageView) findViewById(R.id.hourImage2);
        hourImage3 = (ImageView) findViewById(R.id.hourImage3);
        hourImage4 = (ImageView) findViewById(R.id.hourImage4);
        hourImage5 = (ImageView) findViewById(R.id.hourImage5);

        day1 = (TextView) findViewById(R.id.day1);
        day2 = (TextView) findViewById(R.id.day2);
        day3 = (TextView) findViewById(R.id.day3);
        day4 = (TextView) findViewById(R.id.day4);
        day5 = (TextView) findViewById(R.id.day5);
        dayTemp1 = (TextView) findViewById(R.id.dayTemp1);
        dayTemp2 = (TextView) findViewById(R.id.dayTemp2);
        dayTemp3 = (TextView) findViewById(R.id.dayTemp3);
        dayTemp4 = (TextView) findViewById(R.id.dayTemp4);
        dayTemp5 = (TextView) findViewById(R.id.dayTemp5);
        dayImage1 = (ImageView) findViewById(R.id.dayImage1);
        dayImage2 = (ImageView) findViewById(R.id.dayImage2);
        dayImage3 = (ImageView) findViewById(R.id.dayImage3);
        dayImage4 = (ImageView) findViewById(R.id.dayImage4);
        dayImage5 = (ImageView) findViewById(R.id.dayImage5);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        hour1.setText(getHourAndLabel(hour));
        hour2.setText(getHourAndLabel(hour+1));
        hour3.setText(getHourAndLabel(hour+2));
        hour4.setText(getHourAndLabel(hour+3));
        hour5.setText(getHourAndLabel(hour+4));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(

                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            temperatureText.setText(Math.round(Double.parseDouble(response.getJSONObject("currently").getString("temperature"))) + "°");

                            hourTemp1.setText(Math.round(Double.parseDouble(response.getJSONObject("hourly").getJSONArray("data").getJSONObject(0).getString("temperature"))) + "°");
                            hourTemp2.setText(Math.round(Double.parseDouble(response.getJSONObject("hourly").getJSONArray("data").getJSONObject(1).getString("temperature"))) + "°");
                            hourTemp3.setText(Math.round(Double.parseDouble(response.getJSONObject("hourly").getJSONArray("data").getJSONObject(2).getString("temperature"))) + "°");
                            hourTemp4.setText(Math.round(Double.parseDouble(response.getJSONObject("hourly").getJSONArray("data").getJSONObject(3).getString("temperature"))) + "°");
                            hourTemp5.setText(Math.round(Double.parseDouble(response.getJSONObject("hourly").getJSONArray("data").getJSONObject(4).getString("temperature"))) + "°");

                            String icon1 = response.getJSONObject("hourly").getJSONArray("data").getJSONObject(0).getString("icon");
                            icon1 = icon1.replace("-", "_");
                            hourImage1.setImageResource(getResources().getIdentifier(icon1, "drawable", getPackageName()));

                            String icon2 = response.getJSONObject("hourly").getJSONArray("data").getJSONObject(1).getString("icon");
                            icon2 = icon2.replace("-", "_");
                            hourImage2.setImageResource(getResources().getIdentifier(icon2, "drawable", getPackageName()));

                            String icon3 = response.getJSONObject("hourly").getJSONArray("data").getJSONObject(2).getString("icon");
                            icon3 = icon3.replace("-", "_");
                            hourImage3.setImageResource(getResources().getIdentifier(icon3, "drawable", getPackageName()));

                            String icon4 = response.getJSONObject("hourly").getJSONArray("data").getJSONObject(3).getString("icon");
                            icon4 = icon4.replace("-", "_");
                            hourImage4.setImageResource(getResources().getIdentifier(icon4, "drawable", getPackageName()));

                            String icon5 = response.getJSONObject("hourly").getJSONArray("data").getJSONObject(4).getString("icon");
                            icon5 = icon5.replace("-", "_");
                            hourImage5.setImageResource(getResources().getIdentifier(icon5, "drawable", getPackageName()));

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(Long.parseLong(response.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("time")) * 1000);
                            System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
                            day1.setText(dayList[calendar.get(Calendar.DAY_OF_WEEK)-1]);

                            calendar.setTimeInMillis(Long.parseLong(response.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("time")) * 1000);
                            day2.setText(dayList[calendar.get(Calendar.DAY_OF_WEEK)-1]);

                            calendar.setTimeInMillis(Long.parseLong(response.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("time")) * 1000);
                            day3.setText(dayList[calendar.get(Calendar.DAY_OF_WEEK)-1]);

                            calendar.setTimeInMillis(Long.parseLong(response.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("time")) * 1000);
                            day4.setText(dayList[calendar.get(Calendar.DAY_OF_WEEK)-1]);

                            calendar.setTimeInMillis(Long.parseLong(response.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("time")) * 1000);
                            day5.setText(dayList[calendar.get(Calendar.DAY_OF_WEEK)-1]);

                            dayTemp1.setText(Math.round(Double.parseDouble(response.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("temperatureHigh"))) + "°");
                            dayTemp2.setText(Math.round(Double.parseDouble(response.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("temperatureHigh"))) + "°");
                            dayTemp3.setText(Math.round(Double.parseDouble(response.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("temperatureHigh"))) + "°");
                            dayTemp4.setText(Math.round(Double.parseDouble(response.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("temperatureHigh"))) + "°");
                            dayTemp5.setText(Math.round(Double.parseDouble(response.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("temperatureHigh"))) + "°");

                            String dayIcon1 = response.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("icon");
                            dayIcon1 = dayIcon1.replace("-", "_");
                            dayImage1.setImageResource(getResources().getIdentifier(dayIcon1, "drawable", getPackageName()));

                            String dayIcon2 = response.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("icon");
                            dayIcon2 = dayIcon2.replace("-", "_");
                            dayImage2.setImageResource(getResources().getIdentifier(dayIcon2, "drawable", getPackageName()));

                            String dayIcon3 = response.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("icon");
                            dayIcon3 = dayIcon3.replace("-", "_");
                            dayImage3.setImageResource(getResources().getIdentifier(dayIcon3, "drawable", getPackageName()));

                            String dayIcon4 = response.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("icon");
                            dayIcon4 = dayIcon4.replace("-", "_");
                            dayImage4.setImageResource(getResources().getIdentifier(dayIcon4, "drawable", getPackageName()));

                            String dayIcon5 = response.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("icon");
                            dayIcon5 = dayIcon5.replace("-", "_");
                            dayImage5.setImageResource(getResources().getIdentifier(dayIcon5, "drawable", getPackageName()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(objectRequest);
    }

}
