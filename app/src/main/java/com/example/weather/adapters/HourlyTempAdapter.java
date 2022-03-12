package com.example.weather.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.MainActivity;
import com.example.weather.databinding.HourlyTempBinding;
import com.example.weather.objects.OneCallObject;

import java.util.List;

public class HourlyTempAdapter extends RecyclerView.Adapter<HourlyTempAdapter.HourlyTempViewHolder> {

    List<OneCallObject.Hourly> hourlies;

    public HourlyTempAdapter(List<OneCallObject.Hourly> hourlies) {
        this.hourlies = hourlies;
    }

    @NonNull
    @Override
    public HourlyTempViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HourlyTempBinding binding = HourlyTempBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new HourlyTempViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyTempViewHolder holder, int position) {
        holder.setData(hourlies.get(position));
    }

    @Override
    public int getItemCount() {
        return hourlies.size();
    }


    static class HourlyTempViewHolder extends RecyclerView.ViewHolder {

        HourlyTempBinding binding;

        HourlyTempViewHolder(HourlyTempBinding hourlyTempBinding){
            super(hourlyTempBinding.getRoot());
            binding=hourlyTempBinding;
        }

        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        void setData(OneCallObject.Hourly hourly){
            binding.date.setText(new java.text.SimpleDateFormat("MM.dd.yyyy HH:mm").format(new java.util.Date (hourly.getDt()*1000)));
            binding.temperature.setText((int) hourly.getTemp()+"°");
            binding.description.setText(firstUpperCase(hourly.getWeather().get(0).getDescription()));
            binding.icon.setImageResource(MainActivity.getWeatherIcon(hourly.getWeather().get(0).getIcon()));
        }

        public String firstUpperCase(String word) {
            if (word == null || word.isEmpty()) return "";//или return word;
            return word.substring(0, 1).toUpperCase() + word.substring(1);
        }
    }
}
