package com.dixon.eazzytravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dixon.eazzytravel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       if (SharedPrefManager.getInstance(this).isLoggedIn()){
           finish();
           startActivity(new Intent(this, MainActivity.class));
       }
       editTextUsername = (EditText) findViewById(R.id.editTextUsername);
       editTextPassword = (EditText) findViewById(R.id.editTextPassword);
       progressBar = (ProgressBar) findViewById(R.id.progressBar);

       findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               userLogin();
           }
       });

        findViewById(R.id.btnCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });
    }
    private void userLogin(){
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username)){
            editTextUsername.setError("Password is empty!");
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            editTextPassword.setError("Password is empty!");
            editTextPassword.requestFocus();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("first_name"),
                                userJson.getString("last_name"),
                                userJson.getString("username"),
                                userJson.getString("email")
                        );

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}