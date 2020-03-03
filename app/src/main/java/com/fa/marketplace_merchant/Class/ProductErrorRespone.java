package com.fa.marketplace_merchant.Class;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductErrorRespone {
    @SerializedName("productName")
    List<String> productNameError = new ArrayList<>();
    @SerializedName("productQty")
    List<String> productQtyError = new ArrayList<>();
    @SerializedName("productPrice")
    List<String> productPriceError = new ArrayList<>();

    public List<String> getProductNameError() {
        return productNameError;
    }

    public List<String> getProductQtyError() {
        return productQtyError;
    }

    public List<String> getProductPriceError() {
        return productPriceError;
    }
}