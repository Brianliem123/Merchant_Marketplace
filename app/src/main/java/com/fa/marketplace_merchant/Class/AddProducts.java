package com.fa.marketplace_merchant.Class;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fa.marketplace_merchant.R;

import java.util.Hashtable;
import java.util.Map;

public class AddProducts extends AppCompatActivity {
    private EditText nameP, qtyP, idC, prC;
    private Button btnAdd;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        inisial();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyLoad();
            }
        });

    }

    public void inisial() {
        btnAdd = findViewById(R.id.btn_add_product);
        nameP = findViewById(R.id.product_name);
        qtyP = findViewById(R.id.qty_products);
        idC = findViewById(R.id.cat_id);
        prC = findViewById(R.id.price_id);
    }

        public void VolleyLoad(){

            String url = "http://210.210.154.65:4444/api/products";

            final String merchant = "1";

            final Map<String, String> data = new Hashtable<String, String>();
            data.put("productName", nameP.getText().toString());
            data.put("productQty", qtyP.getText().toString());
            data.put("categoryId", idC.getText().toString());
            data.put("productPrice", prC.getText().toString());
            data.put("merchantId", merchant);
           // data.put("productImage",null);

            requestQueue = Volley.newRequestQueue(this);

            StringRequest addProductReq = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AddProducts.this, "Data Telah Ditambahkan", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddProducts.this, "Data Gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new Hashtable<String, String>();
                    param = data;
                    return param;
                }
            };

            requestQueue.add(addProductReq);
        }
    }