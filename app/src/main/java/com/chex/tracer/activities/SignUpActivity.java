package com.chex.tracer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.UserManager;
import com.chex.tracer.api.models.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditTxt, emailEditTxt, pwdEditTxt;
    private final UserManager userManager = new UserManager();
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        emailEditTxt = findViewById(R.id.signUp_emailEditTxt);
        usernameEditTxt = findViewById(R.id.signUP_usernameEditTxt);
        pwdEditTxt = findViewById(R.id.signUp_pwdEditTxt);

        //Acción para el texto de inicio de sesión; cambia el Activity
        findViewById(R.id.signUp_toLogIn).setOnClickListener(view -> changeActivity(LogInActivity.class));

        findViewById(R.id.signUp_signUpBtn).setOnClickListener(view -> signUp());
    }

    private boolean checkFields(String email, String username, String pwd){
        boolean allRight = true;

        if(email.isEmpty()){
            emailEditTxt.setError(getText(R.string.empty_fields));
            allRight = false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTxt.setError(getText(R.string.invalid_email_format));
            allRight = false;
        }else{
            userManager.isEmailAvailable(email, new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    if(!(Boolean)obj){
                        emailEditTxt.setError(getText(R.string.not_available));
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(SignUpActivity.this, getResources().getText(R.string.something_goes_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(username.isEmpty()){
            usernameEditTxt.setError(getText(R.string.empty_fields));
            allRight = false;
        }else{
            userManager.isUsernameAvailable(username, new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    if(!(Boolean)obj){
                        usernameEditTxt.setError(getText(R.string.not_available));
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(SignUpActivity.this, getResources().getText(R.string.something_goes_wrong), Toast.LENGTH_SHORT).show();
                }
            });
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

    public void signUp(){
        String email = emailEditTxt.getText().toString().trim();
        String username = usernameEditTxt.getText().toString().trim();
        String pwd = pwdEditTxt.getText().toString().trim();

        if(checkFields(email, username, pwd)){
            userManager.signup(username, email, pwd, new APICallBack() {
                @Override
                public void onSuccess(Object obj) {
                    String userId = (String) obj;
                    if(userId != null){
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userId", userId);
                        editor.apply();
                        changeActivity(MainActivity.class);
                    }else{
                        Toast.makeText(SignUpActivity.this, "Sí, está entrando :C", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(SignUpActivity.this, getResources().getText(R.string.something_goes_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}