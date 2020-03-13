package com.fa.marketplace_merchant.Class;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fa.marketplace_merchant.Adapter.CategoriesAdapter;
import com.fa.marketplace_merchant.MainActivity;
import com.fa.marketplace_merchant.Model.AccessToken;
import com.fa.marketplace_merchant.Model.Category;
import com.fa.marketplace_merchant.Model.Product;
import com.fa.marketplace_merchant.R;
import com.fa.marketplace_merchant.Utils.TokenManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner categoryDropdown;
    ArrayList<Category> categories;
    CategoriesAdapter categoriesAdapter;

    private EditText nameP, priceP, desP, qtyP;
    private Button imgAdd, btnEditProduct;
    private ImageView imgP;

    private RequestQueue requestQueue;

    private String slug;
    private Product product;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc, productQty, productPrice, categoryId, merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        requestQueue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            slug = bundle.getString("slug");
        }

        inisial();
        getAllCategories();
        getDataSebelumnya();

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productName = validasiEdt(nameP);
                productDesc = validasiEdt(desP);
                productPrice = validasiEdt(priceP);
                productQty = validasiEdt(qtyP);

                //merchantId = "1";

                if (productImage == null) {
                    productImage = null;
                }

                volleyLoad();

                Intent intent = new Intent(EditProduct.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public String validasiEdt(EditText edt) {
        String text = null;
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Isi dulu cuk , matalu siwer apa");
        } else {
            text = edt.getText().toString();
        }
        return text;
    }

    public  void inisial(){
        //Edit Text
        nameP = findViewById(R.id.product_name_edit);
        qtyP = findViewById(R.id.qty_products_edit);
        priceP= findViewById(R.id.price_id_edit);
        desP = findViewById(R.id.input_item_desc_edit);

        //Button
        btnEditProduct = findViewById(R.id.btn_add_product_edit);
        imgAdd = findViewById(R.id.btn_choose_image_edit);

        //Image view
        imgP = findViewById(R.id.image_form_file_edit);

        categoryDropdown = findViewById(R.id.category_dropdown_edit);
        categories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter();
        categoryDropdown.setAdapter(categoriesAdapter);
        categoryDropdown.setOnItemSelectedListener(this);
    }

    public  void  volleyLoad(){
        AccessToken accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();
        String url = "http://210.210.154.65:4444/api/merchant/product/"+product.getProductId()+"/update";


        StringRequest addProductReq =  new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("respone :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("response :", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(EditProduct.this, "data telah di update", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProduct.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("productName",productName);
                params.put("productDesc",productDesc);
                params.put("productQty",productQty);
                params.put("productPrice", productPrice);

                if (productImage != null){
                    params.put("productImage", productImage);
                }

                params.put("categoryId", categoryId);
                //params.put("merchantId", merchantId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();
                params.put("Content-type","application/x-www-form-urlencoded");
                params.put("Authorization", accessToken.getTokenType() +" "+ accessToken.getAccessToken());
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        addProductReq.setRetryPolicy(retryPolicy);
        requestQueue.add(addProductReq);
        //VolleyApp.getInstance().addToRequestQueue(addProductReq, "edt_product_req");

    }
    private void getDataSebelumnya() {

        String url = "http://210.210.154.65:4444/api/product/" + slug;

        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = response.getJSONObject("data");
                            product = gson.fromJson(jsonObject.toString(), Product.class);

                            nameP.setText(product.getProductName());
                            desP.setText(product.getProductDesc());
                            qtyP.setText(String.valueOf(product.getProductQty()));
                            Glide.with(getApplicationContext()).load("http://210.210.154.65:4444/storage/"+product.getProductImg()).into(imgP);
                            priceP.setText(String.valueOf(product.getProductPrice()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProduct.this, "Data Tidak Terbaca", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(req);
    }
    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    private void getAllCategories(){
        String url = "http://210.210.154.65:4444/api/categories";

        JsonObjectRequest listCatReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                Gson gson = new Gson();
                                Category category = gson.fromJson(data.getJSONObject(i).toString(),Category.class);
                                categories.add(category);
                            }

                            categoriesAdapter.addData(categories);
                            categoriesAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),String.valueOf(categoriesAdapter.getCount()),Toast.LENGTH_LONG).show();

                        }
                        catch (JSONException e){
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


        requestQueue.add(listCatReq);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //encoding image to string
                productImage = getStringImage(bitmap); // call getStringImage() method below this code
                Log.d("image", productImage);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .override(imgP.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imgP);
                System.out.println("image : " + productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // convert image bitmap to string base64
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.categoryId = String.valueOf(categoriesAdapter.getItemId(position));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}