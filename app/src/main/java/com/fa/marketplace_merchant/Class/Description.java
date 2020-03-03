package com.fa.marketplace_merchant.Class;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fa.marketplace_merchant.Model.Product;
import com.fa.marketplace_merchant.R;
import com.google.gson.Gson;

public class Description extends AppCompatActivity {
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9,tv10,tv11;
    private ImageView img;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

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

    public void Pindah(View view) {
        Intent intent = new Intent(Description.this,EditProduct.class);
        intent.putExtra("slug",product.getProductSlug());
        startActivity(intent);
    }
}
