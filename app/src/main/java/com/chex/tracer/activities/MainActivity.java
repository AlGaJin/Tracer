package com.chex.tracer.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chex.tracer.R;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.UserManager;
import com.chex.tracer.api.models.User;
import com.chex.tracer.api.models.Videogame;
import com.chex.tracer.fragments.nav.HomeFragment;
import com.chex.tracer.fragments.nav.ProfileFragment;
import com.chex.tracer.fragments.nav.SettingsFragment;
import com.chex.tracer.fragments.others.EditProfileFragment;
import com.chex.tracer.fragments.others.ReviewFragment;
import com.chex.tracer.fragments.others.VideogameDetailFragment;
import com.chex.tracer.utils.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserViewModel userViewModel;
    private ProgressBar mainPB;
    private ImageView profilePic;
    private SharedPreferences preferences;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle toggle;
    private Deque<Integer> idDeque; // Lista "estática" que tiene funciones de listas dinámicas (útil para Back Stack casero)
    private boolean flag; // Boleano necesario para Back Stack casero
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPB = findViewById(R.id.main_pb);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged();

        idDeque = new ArrayDeque<>(3); // Lista con un número límite de almacenaje (permite que solo se puedan guardar X fragmentos en "memoria")
        flag = true;

        profilePic = findViewById(R.id.toolbar_userProfilePic);
        profilePic.setOnClickListener(view -> changeNavFragment(getFragment(addDeque(R.id.nav_profile), bundle), R.id.nav_profile));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        idDeque.addLast(R.id.nav_home);
        navView.setCheckedItem(R.id.nav_home);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                accionBack(bundle);
            }
        });

        toolbar.setNavigationOnClickListener(view -> {
            if (!toggle.isDrawerIndicatorEnabled()) {
                accionBack(bundle);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void isLogged() {
        String userId = preferences.getString("userId", null);

        if(userId != null && !userId.isEmpty()){
            new UserManager().getUser(Integer.parseInt(userId), new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    userViewModel.setLoggedUser((User)obj);

                    setProfilePic(userViewModel.getLoggedUser().getProfile_pic());

                    mainPB.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }else{
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        }
    }

    public void setProfilePic(String drawableName) {
        try{
            int drawableId = R.drawable.class.getField(drawableName).getInt(null);
            profilePic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawableId, null));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int addDeque(int id){
        if(idDeque.contains(id)){
            if(id == R.id.nav_home && idDeque.size() != 1 && flag){ // Resumidamente: obliga a que el último fragmento que se muestra sea siempre el de inicio
                idDeque.addFirst(R.id.nav_home);
                flag = false;
            }
            idDeque.remove(id); // Elimina una antigua selección para no repetir (Back Stack no lo hace)
        }
        idDeque.push(id);
        return id;
    }

    public Fragment getFragment(int id, Bundle bundle){
        Fragment fgt = new HomeFragment();

        if(id == R.id.nav_home){
            fgt = new HomeFragment();
            showHamburgerBtn(true);
        } else if(id == R.id.nav_profile){
            fgt = new ProfileFragment();
            showHamburgerBtn(false);
        } else if(id == R.id.nav_settings) {
            fgt = new SettingsFragment();
            showHamburgerBtn(false);
        } else if (id == 1) {
            fgt = new VideogameDetailFragment();
            showHamburgerBtn(false);
        }else if (id == 2){
            fgt = new ReviewFragment();
            showHamburgerBtn(false);
        }else if (id == 3){
            fgt = new EditProfileFragment();
            showHamburgerBtn(false);
        }

        fgt.setArguments(bundle);
        return fgt;
    }

    public void accionBack(Bundle bundle){
        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        } else {
            idDeque.pop(); // Elimino el id del fragmento que se está mostrando
            if (!idDeque.isEmpty()) {
                changeNavFragment(getFragment(idDeque.getFirst(), bundle), idDeque.getFirst()); // Muestro el fragmento que se estaba mostrando anteriormente
            } else if (Objects.requireNonNull(getVisibleFragment()).getClass().equals(HomeFragment.class)) {
                finish(); // Si la lista está vacia se cierra el programa
            }
        }
    }

    private Fragment getVisibleFragment(){

        List<Fragment> fragmentos = getSupportFragmentManager().getFragments();
        for (Fragment f : fragmentos)
            if(f.isVisible()) return f;

        return null;
    }

    public void changeNavFragment(Fragment newFgt, int itemId){
        if(!getVisibleFragment().getClass().equals(newFgt.getClass())) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, newFgt).commit();
        }


        Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAA", "changeNavFragment: " + itemId);

        Menu menu = navView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if(itemId == item.getItemId()){
                item.setChecked(true);
            }else{
                item.setChecked(false);
            }
        }
    }

    public void showHamburgerBtn(boolean enabled) {
        toggle.setDrawerIndicatorEnabled(enabled);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(!enabled);
        toggle.syncState();
        if (enabled) {
            profilePic.setVisibility(View.VISIBLE);
        } else {
            profilePic.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        drawerLayout.close();
        changeNavFragment(getFragment(addDeque(itemId), bundle), itemId);
        return true;
    }

    public void changeFragmentWithBundle(int fgtId, Bundle bundle) {
        this.bundle = bundle;
        changeNavFragment(getFragment(addDeque(fgtId), bundle), -1);
    }
}
