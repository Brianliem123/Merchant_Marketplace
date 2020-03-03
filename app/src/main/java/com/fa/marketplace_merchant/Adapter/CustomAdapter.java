package com.fa.marketplace_merchant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fa.marketplace_merchant.Class.Description;
import com.fa.marketplace_merchant.Model.Product;
import com.fa.marketplace_merchant.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Product> products = new ArrayList<>();

    public CustomAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_product,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvNama.setText(products.get(position).getProductName());
        holder.tvMerchant.setText(products.get(position).getMerchant().getMerchantName());
        holder.tvPrice.setText(String.valueOf(products.get(position).getProductPrice()));


        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl+products.get(position).getProductImg();

        Glide.with(context).load(url).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Description.class);
                //intent.putExtra("data",products.get(position));
                String json = new Gson().toJson(products.get(position));
                intent.putExtra("data", json);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void adddata(ArrayList<Product> products) {
        this.products = products;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvMerchant,tvPrice;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_nama_product);
            img = itemView.findViewById(R.id.image_product);
            tvMerchant = itemView.findViewById(R.id.tv_merchant);
            tvPrice = itemView.findViewById(R.id.tv_prc);
        }
    }
}
