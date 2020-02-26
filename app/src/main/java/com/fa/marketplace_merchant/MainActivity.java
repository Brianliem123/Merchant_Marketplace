package com.fa.marketplace_merchant;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fa.marketplace_merchant.Adapter.CustomAdapter;
import com.fa.marketplace_merchant.Class.AddProducts;
import com.fa.marketplace_merchant.Model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    CustomAdapter customAdapter = new CustomAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inisialKomponen();
        volleyEffect();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, AddProducts.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the list_product; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void volleyEffect(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        String url = "http://210.210.154.65:4444/api/products";

        JsonObjectRequest listProductReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<Product> products = new ArrayList<>();
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i<data.length();i++){
                                Gson gson =  new Gson();
                                Product product = gson.fromJson(data.getJSONObject(i).toString(), Product.class);
                                products.add(product);
                            }

                            generateAdapter();
                            customAdapter.adddata(products);
                            customAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), String.valueOf(customAdapter.getItemCount()), Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });

        requestQueue.add(listProductReq);
    }
    public void inisialKomponen(){
        recyclerView = findViewById(R.id.rv_main);
    }

    public void generateAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(customAdapter);

    }
}
