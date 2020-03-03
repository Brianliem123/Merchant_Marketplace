package com.fa.marketplace_merchant.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fa.marketplace_merchant.Model.Category;
import com.fa.marketplace_merchant.R;

import java.util.ArrayList;

public class CategoriesAdapter extends BaseAdapter {

    ArrayList<Category> categories = new ArrayList<>();

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getCategoryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_dropdown_item, parent ,false);
        }
        Category category = getItem(position);
        TextView tvYext =convertView.findViewById(R.id.cat_text);
        tvYext.setText(category.getCategoryName());

        return convertView;
    }

    public void addData(ArrayList<Category> categories){
        this.categories = categories;
    }
}
