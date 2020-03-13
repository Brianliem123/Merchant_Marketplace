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
import com.fa.marketplace_merchant.R;
import com.fa.marketplace_merchant.Utils.TokenManager;
import com.fa.marketplace_merchant.VolleyApp;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class AddProducts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RequestQueue requestQueue;
    Spinner categoryDropDown;
    ArrayList<Category> categories;
    CategoriesAdapter categoriesAdapter;
    ImageView imageView;

    EditText nameP, qtyP, prC, desc;
    private Button btnAdd , btnChoose;

    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null;
    private String productName, productDesc,productQty, productPrice, categoryId, merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        getAllCategories();
        inisial();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = nameP.getText().toString();
                productDesc = desc.getText().toString();
                productPrice = prC.getText().toString();
                productQty = qtyP.getText().toString();


                if(productImage == null) {
                    productImage = null;
                    MainActivity.fa.finish();
                    Intent intent = new Intent(AddProducts.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                VolleyLoad();

            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

    }

    public void inisial() {
        //Button
        btnAdd = findViewById(R.id.btn_add_product);
        btnChoose = findViewById(R.id.btn_choose_image);

        //Edit Text
        nameP = findViewById(R.id.product_name);
        qtyP = findViewById(R.id.qty_products);
        prC = findViewById(R.id.price_id);
        desc = findViewById(R.id.input_item_desc);

        // ImageView for hold choosed image from intent
        imageView = findViewById(R.id.image_form_file);

        //Spinner Categoriesa
        categoryDropDown = findViewById(R.id.category_dropdown);
        // Array list for hold categories from server
        categories = new ArrayList<>();
        // set categories adapter to Spinner
        categoriesAdapter = new CategoriesAdapter();
        categoryDropDown.setAdapter(categoriesAdapter);
        categoryDropDown.setOnItemSelectedListener(this);
        // get all categories from server

    }

        public void VolleyLoad() {
            AccessToken accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();
            String url = "http://210.210.154.65:4444/api/merchant/products";
            final StringRequest addProductReq = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        } else {
                            ProductErrorRespone errorRespone = new Gson().fromJson(jsonObject.getString("message"), ProductErrorRespone.class);
                            if (errorRespone.getProductNameError().size() != 0) {
                                if (errorRespone.getProductNameError().get(0) != null) {
                                    nameP.setError(errorRespone.getProductNameError().get(0));
                                    Toast.makeText(getApplicationContext(), errorRespone.getProductNameError().get(0), Toast.LENGTH_LONG).show();
                                }
                            }

                            if (errorRespone.getProductQtyError().size() != 0) {
                                if (errorRespone.getProductQtyError().get(0) != null) {
                                    qtyP.setError(errorRespone.getProductQtyError().get(0));
                                    Toast.makeText(getApplicationContext(), errorRespone.getProductQtyError().get(0), Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (errorRespone.getProductPriceError().size() != 0) {
                                if (errorRespone.getProductPriceError().get(0) != null) {
                                    prC.setError(errorRespone.getProductPriceError().get(0));
                                    Toast.makeText(getApplicationContext(), errorRespone.getProductPriceError().get(0), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        Log.i("response", response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getApplicationContext(), String.valueOf(error.networkResponse), Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();

                    params.put("productName", productName);
                    params.put("productQty", productQty);
                    params.put("productDesc", productDesc);
                    params.put("productPrice", productPrice);
                    if(productImage != null) {
                        params.put("productImage", productImage);
                    }
                    params.put("categoryId", categoryId);
                    //params.put("merchantId", merchantId);


                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new Hashtable<>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", accessToken.getTokenType() +" "+ accessToken.getAccessToken());
                    return headers;
                }
            };
            {
                 // requestQueue = Volley.newRequestQueue(this);

                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                addProductReq.setRetryPolicy(policy);
  //              RequestQueue requestQueue = Volley.newRequestQueue(this);
//                requestQueue.add(addProductReq);
            }
            //requestQueue.add(addProductReq);
            VolleyApp.getInstance().addToRequestQueue(addProductReq, "add_product_req");
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.categoryId = String.valueOf(categoriesAdapter.getItemId(position));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                Log.d("image",productImage);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .override(imageView.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);

                System.out.println("image : "+productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //bikin baos
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos); // bitmap to baos
        byte[] imageBytes = baos.toByteArray(); // baos to bytearray
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT); //bytearray to base64 string
        return encodedImage;
    }
}