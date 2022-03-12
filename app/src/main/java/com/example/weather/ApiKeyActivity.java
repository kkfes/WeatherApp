package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.weather.databinding.ActivityApiKeyBinding;
import com.sdsmdg.tastytoast.TastyToast;

public class ApiKeyActivity extends AppCompatActivity {

    private ActivityApiKeyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApiKeyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.addAPI.setOnClickListener(v -> addAPI());
    }

    private void addAPI(){
        String txt = binding.apiInput.getText().toString().trim();
        if(txt.isEmpty()){
            TastyToast.makeText(getApplicationContext(),"Введите API ключ",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
        }else {
            TastyToast.makeText(getApplicationContext(),"API ключ обновлён",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
            SharedPreferences.Editor edit = MainActivity.pref.edit();
            edit.putString(MainActivity.API_KEY_KEY, txt);
            edit.apply();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

}