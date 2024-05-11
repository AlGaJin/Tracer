package com.chex.tracer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.chex.tracer.R;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private String loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loggedUserId = preferences.getString("userId", null);
        if(loggedUserId == null || loggedUserId.isEmpty()){
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        }
    }
}