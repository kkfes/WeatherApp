package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.weather.adapters.CitiesAdapter;
import com.example.weather.adapters.HourlyTempAdapter;
import com.example.weather.databinding.ActivityHourlyBinding;

public class HourlyActivity extends AppCompatActivity {

    private ActivityHourlyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHourlyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void loading(Boolean loading){
        if(loading){
            binding.temp.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.GONE);
            binding.temp.setVisibility(View.VISIBLE);
        }
    }

    private void init(){
        loading(true);
        HourlyTempAdapter citiesAdapter = new HourlyTempAdapter(MainActivity.o1.getHourly());
        binding.temp.setAdapter(citiesAdapter);
        loading(false);
    }
}