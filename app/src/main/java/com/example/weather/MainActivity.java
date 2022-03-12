package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.objects.OneCallObject;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LAT_KEY = "lat";
    public static final String LON_KEY = "lon";

    public static final String API_KEY_KEY = "apiKey";

    public static int video_id = 0;

    public static String cityName = "";

    LocationManager locationManager;


    public static SharedPreferences pref;
    public static String API_KEY = "";
    public static final String URL = "https://api.openweathermap.org/data/2.5/onecall";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = getSharedPreferences("weather", MODE_PRIVATE);
        init();
        setListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(o1!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            showNotification(getApplicationContext(),"Погода "+o1.getCurrent().getTemp()+"°",o1.getTimezone()+" • "+firstUpperCase(o1.getCurrent().getWeather().get(0).getDescription()),intent,1);
        }
    }


    private void setListeners() {
        binding.updateWeather.setOnClickListener(v -> updateLocation());
        binding.hourly.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),HourlyActivity.class)));
        binding.addCity.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CitiesActivity.class)));
        binding.days7.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(),ForecastActivity.class);
            String city  =getIntent().getStringExtra("city");
            if(city==null) {
                intent.putExtra("lat", Double.parseDouble(pref.getString(LAT_KEY, "0")));
                intent.putExtra("lon", Double.parseDouble(pref.getString(LON_KEY, "0")));
            }else {
                intent.putExtra("city",city);
            }
            startActivity(intent);
        });
        binding.deleteApi.setOnClickListener(v -> {
            TastyToast.makeText(getApplicationContext(),"API ключ удалён",TastyToast.LENGTH_SHORT,TastyToast.CONFUSING).show();
            SharedPreferences.Editor edit = MainActivity.pref.edit();
            edit.putString(MainActivity.API_KEY_KEY, null);
            edit.apply();
            Intent intent = new Intent(getApplicationContext(),ApiKeyActivity.class);
            startActivity(intent);
            finish();
        });
    }



    private void backgroundUpdating() {
        new Thread(() -> {
            double lat = Double.parseDouble(pref.getString(LAT_KEY, "0"));
            double lon = Double.parseDouble(pref.getString(LON_KEY, "0"));
            if (lat != 0 && lon != 0) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        URL + "?lat=" + lat + "&lon=" + lon + "&units=metric&lang=ru&appid=" + API_KEY,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                OneCallObject o = OneCallObject.getObject(jsonObject);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                showNotification(getApplicationContext(), "Погода " + o.getCurrent().getTemp() + "°", "Обновление в фоновом режиме...", intent, 1);
                            } catch (Exception e) {
                            }
                        },
                        error -> {
                            TastyToast.makeText(getApplicationContext(), "ERROR: " + error, TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }

        }).start();
    }

    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_2)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build());
        Log.d("showNotification", "showNotification: " + reqCode);
    }


    private void loading(Boolean loading) {
        if (loading) {
            binding.progressBar1.setVisibility(View.VISIBLE);
            binding.progressBar2.setVisibility(View.VISIBLE);

            binding.cityName.setVisibility(View.GONE);
            binding.temperature.setVisibility(View.GONE);
            binding.description.setVisibility(View.GONE);
            binding.temperatureIcon.setVisibility(View.GONE);

            binding.day1.setVisibility(View.GONE);
            binding.day2.setVisibility(View.GONE);
            binding.day3.setVisibility(View.GONE);

            binding.day1Description.setVisibility(View.GONE);
            binding.day2Description.setVisibility(View.GONE);
            binding.day3Description.setVisibility(View.GONE);

            binding.day1Temp.setVisibility(View.GONE);
            binding.day2Temp.setVisibility(View.GONE);
            binding.day3Temp.setVisibility(View.GONE);

            binding.now1.setVisibility(View.GONE);
            binding.now2.setVisibility(View.GONE);
            binding.now3.setVisibility(View.GONE);
            binding.now4.setVisibility(View.GONE);

            binding.now1Temp.setVisibility(View.GONE);
            binding.now2Temp.setVisibility(View.GONE);
            binding.now3Temp.setVisibility(View.GONE);
            binding.now4Temp.setVisibility(View.GONE);

            binding.now1Icon.setVisibility(View.GONE);
            binding.now2Icon.setVisibility(View.GONE);
            binding.now3Icon.setVisibility(View.GONE);
            binding.now4Icon.setVisibility(View.GONE);

            binding.now1Temp.setVisibility(View.GONE);
            binding.now2Temp.setVisibility(View.GONE);
            binding.now3Temp.setVisibility(View.GONE);
            binding.now4Temp.setVisibility(View.GONE);

            binding.now1Wind.setVisibility(View.GONE);
            binding.now2Wind.setVisibility(View.GONE);
            binding.now3Wind.setVisibility(View.GONE);
            binding.now4Wind.setVisibility(View.GONE);

            binding.now1WindSpeed.setVisibility(View.GONE);
            binding.now2WindSpeed.setVisibility(View.GONE);
            binding.now3WindSpeed.setVisibility(View.GONE);
            binding.now4WindSpeed.setVisibility(View.GONE);

            binding.days7.setVisibility(View.GONE);
            binding.hourly.setVisibility(View.GONE);
        } else {
            binding.progressBar1.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.GONE);

            binding.cityName.setVisibility(View.VISIBLE);
            binding.temperature.setVisibility(View.VISIBLE);
            binding.description.setVisibility(View.VISIBLE);
            binding.temperatureIcon.setVisibility(View.VISIBLE);

            binding.day1.setVisibility(View.VISIBLE);
            binding.day2.setVisibility(View.VISIBLE);
            binding.day3.setVisibility(View.VISIBLE);

            binding.day1Description.setVisibility(View.VISIBLE);
            binding.day2Description.setVisibility(View.VISIBLE);
            binding.day3Description.setVisibility(View.VISIBLE);

            binding.day1Temp.setVisibility(View.VISIBLE);
            binding.day2Temp.setVisibility(View.VISIBLE);
            binding.day3Temp.setVisibility(View.VISIBLE);

            binding.now1.setVisibility(View.VISIBLE);
            binding.now2.setVisibility(View.VISIBLE);
            binding.now3.setVisibility(View.VISIBLE);
            binding.now4.setVisibility(View.VISIBLE);

            binding.now1Temp.setVisibility(View.VISIBLE);
            binding.now2Temp.setVisibility(View.VISIBLE);
            binding.now3Temp.setVisibility(View.VISIBLE);
            binding.now4Temp.setVisibility(View.VISIBLE);

            binding.now1Icon.setVisibility(View.VISIBLE);
            binding.now2Icon.setVisibility(View.VISIBLE);
            binding.now3Icon.setVisibility(View.VISIBLE);
            binding.now4Icon.setVisibility(View.VISIBLE);

            binding.now1Temp.setVisibility(View.VISIBLE);
            binding.now2Temp.setVisibility(View.VISIBLE);
            binding.now3Temp.setVisibility(View.VISIBLE);
            binding.now4Temp.setVisibility(View.VISIBLE);

            binding.now1Wind.setVisibility(View.VISIBLE);
            binding.now2Wind.setVisibility(View.VISIBLE);
            binding.now3Wind.setVisibility(View.VISIBLE);
            binding.now4Wind.setVisibility(View.VISIBLE);

            binding.now1WindSpeed.setVisibility(View.VISIBLE);
            binding.now2WindSpeed.setVisibility(View.VISIBLE);
            binding.now3WindSpeed.setVisibility(View.VISIBLE);
            binding.now4WindSpeed.setVisibility(View.VISIBLE);

            binding.days7.setVisibility(View.VISIBLE);
            binding.hourly.setVisibility(View.VISIBLE);
        }
    }

    private void loadingUpdateLocation(Boolean loading) {
        if (loading) {
            binding.addCity.setVisibility(View.GONE);
            binding.updateWeather.setVisibility(View.GONE);
            binding.progressBar3.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar3.setVisibility(View.GONE);
            binding.updateWeather.setVisibility(View.VISIBLE);
            binding.addCity.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        String api = pref.getString(API_KEY_KEY,null);
        if(api==null||api.isEmpty()){
            Intent intent = new Intent(getApplicationContext(),ApiKeyActivity.class);
            startActivity(intent);
            finish();
            return;
        }else {
            API_KEY=api;
        }
        double lat = Double.parseDouble(pref.getString(LAT_KEY, "0"));
        double lon = Double.parseDouble(pref.getString(LON_KEY, "0"));
        String city = null;
        try {
            city = getIntent().getStringExtra("city");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (city == null) {
            if (lat == 0 && lon == 0) {
                updateLocation();
                SharedPreferences.Editor edit = pref.edit();
                edit.putString(LAT_KEY, String.valueOf(50));
                edit.putString(LON_KEY, String.valueOf(50));
                edit.apply();
            } else {
                getCityName(lat, lon);
                loadJSONFromUrl(URL + "?lat=" + lat + "&lon=" + lon + "&units=metric&lang=ru&appid=" + API_KEY);
            }
        } else {
            getLatLon(city);
            Log.d("CITY", city);
        }
    }

    public static final String CITIES_KEY = "cities";

    public List<String> getCities() {
        List<String> cities = new ArrayList<>();
        try {

            String[] strings = pref.getString(CITIES_KEY, "").split(",");
            cities.addAll(Arrays.asList(strings));
        } catch (Exception e) {
            e.printStackTrace();
        }
        cities.remove(null);
        cities.remove("");
        return cities;
    }

    public boolean deleteCity(String city) {
        String[] strings = pref.getString(CITIES_KEY, "").split(",");
        int i = 0;
        int num = -1;
        for (String s : strings) {
            if (s.equalsIgnoreCase(city)) {
                num = i;
                break;
            }
            i++;
        }
        if (num != -1) {
            strings[num] = "";
        } else {
            return false;
        }
        StringBuilder cities = new StringBuilder();
        for (String s : strings) {
            if (!s.isEmpty()) {
                cities.append(s).append(",");
            }
        }
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(CITIES_KEY, cities.toString());
        edit.apply();
        return true;
    }


    private void updateLocation() {
        TastyToast.makeText(getApplicationContext(), "Обновляю локацию", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
        loadingUpdateLocation(true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1000, location -> {
            loadingUpdateLocation(false);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(LAT_KEY, String.valueOf(location.getLatitude()));
            edit.putString(LON_KEY, String.valueOf(location.getLongitude()));
            edit.apply();
            init();
            TastyToast.makeText(getApplicationContext(), "Локация обновлена", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
        });
    }

    public static OneCallObject o1 = null;


    private void loadJSONFromUrl(String url) {
        loading(true);
        @SuppressLint({"SetTextI18n", "SimpleDateFormat", "ResourceAsColor"}) StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                response -> {
                    loading(false);
                    TastyToast.makeText(getApplicationContext(), "Погода загружена!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        OneCallObject o = OneCallObject.getObject(jsonObject);
                        o1=o;
                        binding.temperature.setText(String.valueOf((int) o.getCurrent().getTemp()));
                        binding.description.setText(firstUpperCase(o.getCurrent().getWeather().get(0).getDescription()));
                        binding.cityName.setText(cityName);

                        binding.day1.setBackgroundResource(getWeatherIcon(o.getDaily().get(0).getWeather().get(0).getIcon()));
                        binding.day2.setBackgroundResource(getWeatherIcon(o.getDaily().get(1).getWeather().get(0).getIcon()));
                        binding.day3.setBackgroundResource(getWeatherIcon(o.getDaily().get(2).getWeather().get(0).getIcon()));


                        binding.day1Description.setText("Сегодня • " + firstUpperCase(o.getDaily().get(0).getWeather().get(0).getDescription()));
                        binding.day2Description.setText("Завтра • " + firstUpperCase(o.getDaily().get(1).getWeather().get(0).getDescription()));
                        binding.day3Description.setText(firstUpperCase(new java.text.SimpleDateFormat("E").format(new java.util.Date(o.getDaily().get(2).getDt() * 1000))) + " • " + firstUpperCase(o.getDaily().get(2).getWeather().get(0).getDescription()));
                        binding.day1Description.setSelected(true);
                        binding.day2Description.setSelected(true);
                        binding.day3Description.setSelected(true);

                        binding.day1Temp.setText((int)o.getDaily().get(0).getTemp().getDay() + "° / " + (int)o.getDaily().get(0).getTemp().getNight() + "°");
                        binding.day2Temp.setText((int)o.getDaily().get(1).getTemp().getDay() + "° / " + (int)o.getDaily().get(1).getTemp().getNight() + "°");
                        binding.day3Temp.setText((int)o.getDaily().get(2).getTemp().getDay() + "° / " + (int)o.getDaily().get(2).getTemp().getNight() + "°");

                        binding.now1.setText("Сейчас");
                        binding.now2.setText(new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(o.getHourly().get(1).getDt() * 1000)));
                        binding.now3.setText(new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(o.getHourly().get(2).getDt() * 1000)));
                        binding.now4.setText(new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(o.getHourly().get(3).getDt() * 1000)));

                        binding.now1Temp.setText((int) o.getHourly().get(0).getTemp() + "°");
                        binding.now2Temp.setText((int) o.getHourly().get(1).getTemp() + "°");
                        binding.now3Temp.setText((int) o.getHourly().get(2).getTemp() + "°");
                        binding.now4Temp.setText((int) o.getHourly().get(3).getTemp() + "°");

                        binding.now1Icon.setBackgroundResource(getWeatherIcon(o.getHourly().get(0).getWeather().get(0).getIcon()));
                        binding.now2Icon.setBackgroundResource(getWeatherIcon(o.getHourly().get(1).getWeather().get(0).getIcon()));
                        binding.now3Icon.setBackgroundResource(getWeatherIcon(o.getHourly().get(2).getWeather().get(0).getIcon()));
                        binding.now4Icon.setBackgroundResource(getWeatherIcon(o.getHourly().get(3).getWeather().get(0).getIcon()));

                        binding.now1Wind.setImageResource(getWindDirection(o.getHourly().get(0).getWind_deg()));
                        binding.now2Wind.setImageResource(getWindDirection(o.getHourly().get(1).getWind_deg()));
                        binding.now3Wind.setImageResource(getWindDirection(o.getHourly().get(2).getWind_deg()));
                        binding.now4Wind.setImageResource(getWindDirection(o.getHourly().get(3).getWind_deg()));

                        binding.now1WindSpeed.setText(o.getHourly().get(0).getWind_speed() + "км/ч");
                        binding.now2WindSpeed.setText(o.getHourly().get(1).getWind_speed() + "км/ч");
                        binding.now3WindSpeed.setText(o.getHourly().get(2).getWind_speed() + "км/ч");
                        binding.now4WindSpeed.setText(o.getHourly().get(3).getWind_speed() + "км/ч");

                        binding.video.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
                        Uri uri;
                        if(o.getCurrent().getWeather().get(0).getIcon().contains("d")){
                            uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.day);
                            video_id=R.raw.day;
                        }else {
                            uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.night);
                            video_id=R.raw.night;
                        }
                        binding.video.setVideoURI(uri);
                        binding.video.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    TastyToast.makeText(getApplicationContext(), "ERROR: " + error, TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(video_id!=0) {
            binding.video.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + video_id);
            binding.video.setVideoURI(uri);
            binding.video.start();
        }
    }

    private int getWindDirection(int degree) {
        if (degree >= 345 || degree <= 15) {
            return R.drawable.up_arrow;
        } else if (degree < 75) {
            return R.drawable.right_up_arrow;
        } else if (degree <= 105) {
            return R.drawable.right_arrow;
        } else if (degree < 165) {
            return R.drawable.right_down_arrow;
        } else if (degree <= 195) {
            return R.drawable.down_arrow;
        } else if (degree < 255) {
            return R.drawable.left_down_arrow;
        } else if (degree <= 285) {
            return R.drawable.left_arrow;
        } else {
            return R.drawable.left_up_arrow;
        }
    }


    public static int getWeatherIcon(String icon) {
        if (icon.contains("d")) {
            String txt = icon.substring(0, 2).trim();
            if (txt.equals("01")) {
                return R.drawable.d01;
            } else if (txt.equals("02")) {
                return R.drawable.d02;
            } else if (txt.equals("03")) {
                return R.drawable.d03;
            } else if (txt.equals("04")) {
                return R.drawable.d04;
            } else if (txt.equals("09")) {
                return R.drawable.d09;
            } else if (txt.equals("10")) {
                return R.drawable.d10;
            } else if (txt.equals("11")) {
                return R.drawable.d11;
            } else if (txt.equals("13")) {
                return R.drawable.d13;
            } else {
                return R.drawable.d50;
            }
        } else {
            String txt = icon.substring(0, 2).trim();
            if (txt.equals("01")) {
                return R.drawable.n01;
            } else if (txt.equals("02")) {
                return R.drawable.n02;
            } else if (txt.equals("03")) {
                return R.drawable.d03;
            } else if (txt.equals("04")) {
                return R.drawable.d04;
            } else if (txt.equals("09")) {
                return R.drawable.d09;
            } else if (txt.equals("10")) {
                return R.drawable.n10;
            } else if (txt.equals("11")) {
                return R.drawable.d11;
            } else if (txt.equals("13")) {
                return R.drawable.d13;
            } else {
                return R.drawable.d50;
            }
        }
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";//или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private void getCityName(double lat, double lon) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        cityName = jsonObject.getString("name");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    TastyToast.makeText(getApplicationContext(), "ERROR: " + error, TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getLatLon(String city) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("CITY", jsonObject.getString("name"));
                        cityName = jsonObject.getString("name");
                        loadJSONFromUrl(URL + "?lat=" + jsonObject.getJSONObject("coord").getDouble("lat") + "&lon=" + jsonObject.getJSONObject("coord").getDouble("lon") + "&units=metric&lang=ru&appid=" + API_KEY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    TastyToast.makeText(getApplicationContext(), "ERROR: " + error, TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }













}