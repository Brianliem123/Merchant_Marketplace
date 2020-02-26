package com.fa.marketplace_merchant.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int productId;
    private int productQty,productPrice;
    private String productName, productSlug;
    private String productImg;

    private Merchant merchant;
    private Category category;

    public int getProductPrice() {
        return productPrice;
    }

    public Product(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductId() {
        return productId;
    }

    public int getProductQty() {
        return productQty;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public String getProductImg() {
        return productImg;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Category getCategory() {
        return category;
    }

    public Product(int productId, int productQty, String productName, String productSlug, String productImg, Merchant merchant, Category category) {
        this.productId = productId;
        this.productQty = productQty;
        this.productName = productName;
        this.productSlug = productSlug;
        this.productImg = productImg;
        this.merchant = merchant;
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeInt(this.productQty);
        dest.writeInt(this.productPrice);
        dest.writeString(this.productName);
        dest.writeString(this.productSlug);
        dest.writeString(this.productImg);
        dest.writeParcelable(this.merchant, flags);
        dest.writeParcelable(this.category, flags);
    }

    protected Product(Parcel in) {
        this.productId = in.readInt();
        this.productQty = in.readInt();
        this.productPrice = in.readInt();
        this.productName = in.readString();
        this.productSlug = in.readString();
        this.productImg = in.readString();
        this.merchant = in.readParcelable(Merchant.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
