package com.fa.marketplace_merchant.Class;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.fa.marketplace_merchant.Model.AccessToken;
import com.fa.marketplace_merchant.Model.Product;
import com.fa.marketplace_merchant.R;
import com.fa.marketplace_merchant.Utils.TokenManager;
import com.fa.marketplace_merchant.VolleyApp;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Description extends AppCompatActivity {
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11;
    private ImageView img;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString("data");

        product = new Gson().fromJson(json, Product.class);

        //Product product = getIntent().getParcelableExtra("data");
        inisialisasi();

        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl + product.getProductImg();

        Glide.with(this).load(url).into(img);

//        Glide.with(this).load(product.getProductImage()).into(img);
        tv1.setText("ID Produk      :  " + String.valueOf(product.getProductId()));
        tv2.setText("Nama Produk    :  " + product.getProductName());
        tv3.setText("Slug Produk    :  " + product.getProductSlug());
        tv4.setText("Qty Produk     :  " + String.valueOf(product.getProductQty()));
        tv5.setText("ID Merchant    :  " + String.valueOf(product.getMerchant().getMerchantId()));
        tv6.setText("Nama Merchant  :  " + product.getMerchant().getMerchantName());
        tv7.setText("Slug Merchant  :  " + product.getMerchant().getMerchantSlug());
        tv8.setText("ID Kategori    :  " + String.valueOf(product.getCategory().getCategoryId()));
        tv9.setText("Nama Kategori  :  " + product.getCategory().getCategoryName());
        tv10.setText("RP .  " + String.valueOf(product.getProductPrice()));
        tv11.setText(product.getProductDesc());

    }

    public void inisialisasi() {
        img = findViewById(R.id.image_product);
        tv1 = findViewById(R.id.tv_des_id);
        tv2 = findViewById(R.id.tv_des_nama);
        tv3 = findViewById(R.id.tv_des_slug);
        tv4 = findViewById(R.id.tv_des_qty);
        tv5 = findViewById(R.id.tv_des_mid);
        tv6 = findViewById(R.id.tv_des_mname);
        tv7 = findViewById(R.id.tv_des_mslug);
        tv8 = findViewById(R.id.tv_des_cid);
        tv9 = findViewById(R.id.tv_des_cname);
        tv10 = findViewById(R.id.tv_des_price);
        tv11 = findViewById(R.id.tv_deskripsi);
    }
    @OnClick(R.id.delete_btn)
    public  void delete(){
        deleteProduct();
    }


    public void Pindah(View view) {
        Intent intent = new Intent(Description.this, EditProduct.class);
        intent.putExtra("slug", product.getProductSlug());
        startActivity(intent);
    }
    public void deleteProduct(){
        AccessToken accessToken = TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).getToken();
        String accessTok = accessToken.getAccessToken();
        String bearer = accessToken.getTokenType();
        String url = "http://210.210.154.65:4444/api/merchant/product/"+ product.getProductId()+"/delete";
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        Toast.makeText(Description.this, "PRODUVT TELAH DI HAPUS", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Description.this, "PRODUCT GAGAL DI HAPUS", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", bearer + " " + accessTok);
                return headers;
            }
        };
        VolleyApp.getInstance().addToRequestQueue(req, "delete_product");
    }
}
