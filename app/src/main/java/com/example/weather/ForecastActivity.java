package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import com.example.weather.databinding.ActivityForecastBinding;
import com.example.weather.objects.OneCallObject;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;


public class ForecastActivity extends AppCompatActivity {

    private ActivityForecastBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForecastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    @SuppressLint("SimpleDateFormat")
    private void init() {
        loading(true);
        LineDataSet lineDataSet = new LineDataSet(getDataSet(),"Днём");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        loading(false);
        binding.forecast.setData(lineData);
        binding.forecast.invalidate();
        binding.forecast.setBackgroundResource(R.color.white);
    }

    @SuppressLint("SimpleDateFormat")
    private ArrayList<Entry> getDataSet(){
        ArrayList<Entry> entries = new ArrayList<>();
        for (OneCallObject.Daily d:MainActivity.o1.getDaily()){
            entries.add(new BarEntry(Float.parseFloat(new java.text.SimpleDateFormat("dd").format(new java.util.Date (d.getDt()*1000))),(float) d.getTemp().getDay()));
        }
        return entries;
    }

    @SuppressLint("SimpleDateFormat")
    private ArrayList<Entry> getDataSetNight(){
        ArrayList<Entry> entries = new ArrayList<>();
        for (OneCallObject.Daily d:MainActivity.o1.getDaily()){
            entries.add(new BarEntry((float) d.getTemp().getDay(), Float.parseFloat(new java.text.SimpleDateFormat("dd").format(new java.util.Date (d.getDt()*1000)))));
        }
        return entries;
    }


    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void loading(boolean loading) {
        if (loading) {
            binding.forecast.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.forecast.setVisibility(View.VISIBLE);
        }
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}