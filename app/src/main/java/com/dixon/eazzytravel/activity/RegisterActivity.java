package com.dixon.eazzytravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dixon.eazzytravel.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFirstName, editTextLastName, editTextUsername, editTextEmail, editTextPassword, editTextPassword1;
     ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword1 = (EditText) findViewById(R.id.editTextRe_enterPassword);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        findViewById(R.id.btnGoToLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }
        });
    }
    private void registerUser(){
        final String first_name = editTextFirstName.getText().toString().trim();
        final String last_name = editTextLastName.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String password1 = editTextPassword1.getText().toString().trim();

        if (TextUtils.isEmpty(first_name)){
            editTextFirstName.setError("Please Enter your First Name");
            editTextFirstName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(last_name)){
            editTextLastName.setError("Please Enter your Last Name");
            editTextLastName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(username)){
            editTextUsername.setError("Please Enter Username");
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)){
            editTextEmail.setError("Please Enter Your Email ");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Invalid Email");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            editTextPassword.setError("Create a Password!");
            editTextPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password1)){
            editTextPassword1.setError("Please Re-Enter Password");
            editTextPassword1.requestFocus();
            return;
        }
        if (!password.equals(password1)){
            editTextPassword1.setError("Password do not match");
            editTextPassword1.requestFocus();
            return;
        }
        if (password.length() < 6){
            editTextPassword.setError("Password too short");
            editTextPassword.requestFocus();
            return;

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                JSONObject userjson = obj.getJSONObject("user");

                                User user = new User(
                                        userjson.getInt("id"),
                                        userjson.getString("first_name"),
                                        userjson.getString("last_name"),
                                        userjson.getString("username"),
                                        userjson.getString("email")
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
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}