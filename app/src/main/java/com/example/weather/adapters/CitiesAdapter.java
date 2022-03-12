package com.example.weather.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.databinding.CityBinding;
import com.example.weather.listeners.CityListener;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    private final List<String> cities;
    private final CityListener cityListener;

    public CitiesAdapter(List<String> cities, CityListener cityListener) {
        this.cities = cities;
        this.cityListener = cityListener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CityBinding cityBinding = CityBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CityViewHolder(cityBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.setCityData(cities.get(position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder{

        CityBinding binding;

        CityViewHolder(CityBinding cityBinding){
            super(cityBinding.getRoot());
            binding=cityBinding;
        }

        void setCityData(String city){
            binding.cityName.setText(city);
            binding.cityName.setOnClickListener(v -> cityListener.onCityClicked(city));
            binding.delete.setOnClickListener(v -> cityListener.onDeleteClicked(city));
        }
    }
}
