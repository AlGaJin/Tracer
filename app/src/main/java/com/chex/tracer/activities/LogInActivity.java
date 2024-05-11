package com.chex.tracer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.UserManager;
import com.chex.tracer.api.models.User;

public class LogInActivity extends AppCompatActivity {
    private EditText userEditTxt, pwdEditTxt;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        userEditTxt = findViewById(R.id.logIn_userEditTxt);
        pwdEditTxt = findViewById(R.id.logIn_pwdEditTxt);

        //Acción para el botón de registro; cambia el Activity
        findViewById(R.id.login_signUp).setOnClickListener(view -> changeActivity(SignUpActivity.class));

        findViewById(R.id.login_loginBtn).setOnClickListener(view -> login());
    }

    private boolean checkFields(String user, String pwd){
        boolean allRight = true;

        if(user.isEmpty()){
            userEditTxt.setError(getText(R.string.empty_fields));
            allRight = false;
        }
        if(pwd.isEmpty()){
            pwdEditTxt.setError(getText(R.string.empty_fields));
            allRight = false;
        }
        return allRight;
    }

    private void changeActivity(Class<?> c){
        startActivity(new Intent(this, c));
        finish();
    }

    private void login(){
        String userString = userEditTxt.getText().toString().trim();
        String pwdString = pwdEditTxt.getText().toString().trim();

        if(checkFields(userString, pwdString)){
            new UserManager().login(userString, pwdString, new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    String userId = (String) obj;
                    if(userId != null){
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userId", userId);
                        editor.apply();

                        changeActivity(MainActivity.class);
                    }else{
                        pwdEditTxt.setText("");
                        pwdEditTxt.setError(getText(R.string.invalid_login));
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(LogInActivity.this, getText(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}