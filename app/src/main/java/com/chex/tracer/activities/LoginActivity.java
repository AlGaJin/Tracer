package com.chex.tracer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.chex.tracer.R;
import com.chex.tracer.api.managers.UserManager;

public class LoginActivity extends AppCompatActivity {
    private EditText userEditTxt, pwdEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEditTxt = findViewById(R.id.logIn_userEditTxt);
        pwdEditTxt = findViewById(R.id.logIn_pwdEditTxt);

        //Acción para el botón de registro; cambia el Activity
        findViewById(R.id.login_signUp).setOnClickListener(view -> changeActivity(SignUpActivity.class));

        findViewById(R.id.login_loginBtn).setOnClickListener(view -> login());
    }

    private boolean checkFields(String user, String pwd){
        boolean allRight = true;

        if(user.isEmpty()){
            userEditTxt.setError("Rellene todos los campos");
            allRight = false;
        }
        if(pwd.isEmpty()){
            pwdEditTxt.setError("Rellene todos los campos");
            allRight = false;
        }
        return allRight;
    }

    private void changeActivity(Class<?> activity){
        startActivity(new Intent(this, activity));
        finish();
    }

    private void login(){
        String userString = userEditTxt.getText().toString().trim();
        String pwdString = pwdEditTxt.getText().toString().trim();

        if(checkFields(userString, pwdString)){
            Log.d("AQUIIIIIII", "login: " + new UserManager().login(userString, pwdString));
        }
    }
}