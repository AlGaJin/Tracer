package com.chex.tracer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.chex.tracer.R;
import com.chex.tracer.fragments.nav.HomeFragment;
import com.chex.tracer.fragments.nav.InfoFragment;
import com.chex.tracer.fragments.nav.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences preferences;
    private String loggedUserId;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            changeNavFragment(new HomeFragment(), R.id.nav_home);
        }
    }

    private void changeNavFragment(Fragment newFgt, int itemId){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFgt).commit();
        navView.setCheckedItem(itemId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Fragment newFgt = null;

        if(itemId == R.id.nav_home){
            newFgt = new HomeFragment();
        } else if(itemId == R.id.nav_info){
            newFgt = new InfoFragment();
        } else if(itemId == R.id.nav_settings){
            newFgt = new SettingsFragment();
        }
        drawerLayout.close();
        changeNavFragment(newFgt, itemId);
        return true;
    }
}