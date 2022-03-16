package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather.adapters.CitiesAdapter;
import com.example.weather.databinding.ActivityCitiesBinding;
import com.example.weather.listeners.CityListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class CitiesActivity extends AppCompatActivity implements CityListener {

    private static final String API_KEY = MainActivity.API_KEY;
    private ActivityCitiesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void setListeners(){
        binding.addCity.setOnClickListener(v -> {
            addCity();
        });
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void addCity(){
        new TestThread().start();
        binding.search.setText(null);
        addCityLoading(true);
    }

    class TestThread extends Thread{
        Context context = getApplicationContext();
        public void run() {
            Looper.prepare();
            if(!binding.search.getText().toString().trim().isEmpty()){
                String city = binding.search.getText().toString().trim();
                if(checkValidCity(city,context)){
                    if(addCity(city,MainActivity.pref)) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("city",city);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(getApplicationContext(),CitiesActivity.class);
                        intent.putExtra("open","exist");
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(),CitiesActivity.class);
                    intent.putExtra("open","no");
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(getApplicationContext(),CitiesActivity.class);
                intent.putExtra("open","print");
                startActivity(intent);
            }
            finish();
        }



    }

    public boolean addCity(String city,SharedPreferences pref){
        try {
            String[] strings = pref.getString(MainActivity.CITIES_KEY,"").split(",");
            for (String s:strings){
                if(s.equalsIgnoreCase(city)){
                    return false;
                }
            }
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(MainActivity.CITIES_KEY,pref.getString(MainActivity.CITIES_KEY,"")+","+city);
            edit.apply();
        }catch (Exception e){
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(MainActivity.CITIES_KEY,city+",");
            edit.apply();
        }
        return true;
    }

    @Override
    public void onCityClicked(String city) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("city",city);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteClicked(String city) {
        TastyToast.makeText(getApplicationContext(),"Город удалён",TastyToast.LENGTH_SHORT,TastyToast.CONFUSING).show();
        new MainActivity().deleteCity(city);
        init();
    }

    private void loading(Boolean loading){
        if(loading){
            binding.cities.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.GONE);
            binding.cities.setVisibility(View.VISIBLE);
        }
    }

    private void addCityLoading(Boolean loading){
        if(loading){
            binding.addCity.setVisibility(View.GONE);
            binding.progressBar1.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar1.setVisibility(View.GONE);
            binding.addCity.setVisibility(View.VISIBLE);
        }
    }

    private static boolean canAdd = true;
    public boolean checkValidCity(String city, Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY,
                response -> {
                    if(response.equals("{\"cod\":\"404\",\"message\":\"city not found\"}")){
                        canAdd=false;
                    }
                },
                error -> canAdd=false
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return canAdd;
    }

    public void init(){
        try {
            String what = getIntent().getStringExtra("open");
            switch (what) {
                case "exist":
                    TastyToast.makeText(getApplicationContext(), "Городу уже иммется", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    break;
                case "no":
                    TastyToast.makeText(getApplicationContext(), "Такого города не существует", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    break;
                case "print":
                    TastyToast.makeText(getApplicationContext(), "Введите название города", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                    break;
            }
        }catch (Exception ignored){
        }
        loading(true);
        CitiesAdapter citiesAdapter = new CitiesAdapter(new MainActivity().getCities(),this);
        binding.cities.setAdapter(citiesAdapter);
        loading(false);
    }

}