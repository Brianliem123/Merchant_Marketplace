package com.fa.marketplace_merchant.Class;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fa.marketplace_merchant.MainActivity;
import com.fa.marketplace_merchant.Model.AccessToken;
import com.fa.marketplace_merchant.R;
import com.fa.marketplace_merchant.Utils.TokenManager;
import com.google.gson.Gson;

import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etEmailLogin)
    EditText editTextEmailLogin;
    @BindView(R.id.etPasswordLogin)
    EditText editTextPasswordLogin;

    ConnectivityManager conMgr;

    RequestQueue requestQueue;
    AccessToken accessTokem;

    final String EMAIL = "email";
    final String PASSWORD = "password";

    String email, password;
    int isMerchant = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        requestQueue = Volley.newRequestQueue(this);

    }

    public void pindah(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()){
                if (isValidInput() == true) {
                    postDataLogin();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }
    } 

    private boolean isValidInput() {
        boolean isValid = true;

        if (editTextEmailLogin.getText().toString().isEmpty()) {
            editTextEmailLogin.setError("email name cannot be empty");
            isValid = false;
        } else if (!editTextEmailLogin.getText().toString().contains("@")) {
            editTextEmailLogin.setError("must be a valid email");
            isValid = false;
        }

        if (editTextPasswordLogin.getText().toString().isEmpty()) {
            editTextPasswordLogin.setError("Password cannot be empty");
            isValid = false;
        } else if (editTextPasswordLogin.getText().toString().length() < 8) {
            editTextPasswordLogin.setError("Password must be 8 or more character");
            isValid = false;

        }

        return isValid;
    }

    private void postDataLogin() {
        email = editTextEmailLogin.getText().toString();
        password = editTextPasswordLogin.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/login";
        StringRequest registerReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // do whatever u want with response
                        accessTokem = new Gson().fromJson(response, AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken(accessTokem);
                        //jika login berhasil
                        Toast.makeText(getApplicationContext(), "LOGIN SUKSES",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statuscode = String.valueOf(error.networkResponse.statusCode);
                        Toast.makeText(LoginActivity.this, statuscode, Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Username atau Password Anda salah!")
                                .setNegativeButton("Retry", null).create().show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();

                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put(EMAIL, email);
                params.put(PASSWORD, password);

                return params;
            }
        };
        requestQueue.add(registerReq);
    }
}