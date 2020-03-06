package com.fa.marketplace_merchant.Class;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fa.marketplace_merchant.Model.AccessToken;
import com.fa.marketplace_merchant.R;
import com.fa.marketplace_merchant.Utils.TokenManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_firstName)
    EditText editTextFirstName;
    @BindView(R.id.et_lastName)
    EditText editTextLastName;
    @BindView(R.id.et_Email)
    EditText editTextEmail;
    @BindView(R.id.et_Password)
    EditText editTextPassword;
    @BindView(R.id.et_confirmPassword)
    EditText editTextConfirmPassword;
    @BindView(R.id.et_merchantName)
    EditText editTextMerchantName;

    RequestQueue requestQueue;
    AccessToken accessTokem;

    ConnectivityManager conMgr;


    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String EMAIL = "email";
    final String PASSWORD = "password";
    final String CPASSWORD = "confirm_password";
    final String IS_MERCHANT = "is_merchant";
    final String MERCHANT_NAME = "merchant_name";

    String firstName, lastName, email, password, confirmPassword, merchantName;
    int isMerchant = 1; // set 1 for true, set 1 in merchant app, and 0 in customer app


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);
    }

    @OnClick(R.id.btn_login_InReg)
    public void goToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.click_register)
    public void register() {
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()){
                if (isValidInput() == true) {
                    postDataRegister();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isValidInput() {
        boolean isValid = true;

        if (editTextFirstName.getText().toString().isEmpty()) {
            editTextFirstName.setError("First name cannot be empty");
            isValid = false;
        }

        if (editTextLastName.getText().toString().isEmpty()) {
            editTextLastName.setError("Last name cannot be empty");
            isValid = false;
        }

        if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError("email name cannot be empty");
            isValid = false;
        } else if (!editTextEmail.getText().toString().contains("@")) {
            editTextEmail.setError("must be a valid email");
            isValid = false;
        }

        if (editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setError("Password cannot be empty");
            isValid = false;
        } else if (editTextPassword.getText().toString().length() < 8) {
            editTextPassword.setError("Password must be 8 or more character");
            isValid = false;
        }

        if (editTextConfirmPassword.getText().toString().isEmpty()) {
            editTextConfirmPassword.setError("Confirm password cannot be empty");
            isValid = false;
        } else if (!(editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString()))) {
            editTextConfirmPassword.setError("Password did not match");
            isValid = false;
        }

        return isValid;
    }

    private void postDataRegister () {
        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        confirmPassword = editTextConfirmPassword.getText().toString();
        isMerchant = 1;
        merchantName = editTextMerchantName.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/signup";
        StringRequest registerReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // do whatever u want with response
                        accessTokem = new Gson().fromJson(response, AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(accessTokem);
                        Toast.makeText(getApplicationContext(), "Berhasil di daftarkan", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statuscode = String.valueOf(error.networkResponse.statusCode);
                        String body = "";
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);
                            RegisterErrorRespone errorResponse = new Gson().fromJson(res.getJSONObject("error").toString(), RegisterErrorRespone.class);
                            if (errorResponse.getEmailError().size() > 0) {
                                if (errorResponse.getEmailError().get(0) != null) {
                                    editTextEmail.setError(errorResponse.getEmailError().get(0));


                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
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

                params.put(FIRST_NAME, firstName);
                params.put(LAST_NAME, lastName);
                params.put(EMAIL, email);
                params.put(PASSWORD, password);
                params.put(CPASSWORD, confirmPassword);
                params.put(IS_MERCHANT, String.valueOf(isMerchant));
                params.put(MERCHANT_NAME, merchantName);

                return params;
            }
        };
        requestQueue.add(registerReq);
    }

}