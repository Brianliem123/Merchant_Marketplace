package com.fa.marketplace_merchant.Class;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fa.marketplace_merchant.Model.AccessToken;
import com.fa.marketplace_merchant.R;
import com.fa.marketplace_merchant.Utils.TokenManager;
import com.fa.marketplace_merchant.VolleyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.tv_name_profile_isi)
    TextView profilName;
    @BindView(R.id.tv_email_profile_isi)
    TextView profilEmail;
    @BindView(R.id.tv_name_merchant_profile_isi)
    TextView profilMerchantName;
    @BindView(R.id.tv_create_date_profile_isi)
    TextView profilCreateAt;
    @BindView(R.id.angka_post)
    TextView angkaPost;

    RegisterActivity registerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        getDataUser();
    }
    @OnClick(R.id.btn_logout_profile)
    public void logout(){
        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).deleteToken();
        Intent intent = new Intent(this, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void getDataUser() {
        String url = " http://210.210.154.65:4444/api/auth/getuser/";
        AccessToken accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();
        String accesTok = accessToken.getAccessToken();
        StringRequest userProfileReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject user = jsonObject.getJSONObject("user");
                            JSONArray productJA = jsonObject.getJSONArray("products");
                            int totalproduct = productJA.length();
                            angkaPost.setText(String.valueOf(totalproduct));

                            String firstName = user.getString("first_name");
                            String lastName = user.getString("last_name");
                            String fullName = firstName + " "+ lastName;
                            Toast.makeText(getApplicationContext(), fullName, Toast.LENGTH_SHORT).show();
                            profilName.setText(fullName);
                            profilEmail.setText(user.getString("email"));
                            profilCreateAt.setText(user.getString("created_at"));

                            JSONObject merchant = user.getJSONObject("merchant");
                            profilMerchantName.setText(merchant.getString("merchantName"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        Toast.makeText(ProfileActivity.this,statusCode,Toast.LENGTH_LONG).show();
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new Hashtable<>();
                header.put("Accept","application/json");
                header.put("Authorization",accessToken.getTokenType()+" "+accesTok);

                return header;
            }
        };

        VolleyApp.getInstance().addToRequestQueue(userProfileReq, "user_req");
    }
}