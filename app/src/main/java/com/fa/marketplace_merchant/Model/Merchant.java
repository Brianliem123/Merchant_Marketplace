package com.fa.marketplace_merchant.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Merchant implements Parcelable {
    int merchantId;
    String merchantName , merchantSlug;

    public int getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantSlug() {
        return merchantSlug;
    }

    public Merchant(int merchantId, String merchantName, String merchantSlug) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.merchantSlug = merchantSlug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.merchantId);
        dest.writeString(this.merchantName);
        dest.writeString(this.merchantSlug);
    }

    protected Merchant(Parcel in) {
        this.merchantId = in.readInt();
        this.merchantName = in.readString();
        this.merchantSlug = in.readString();
    }

    public static final Parcelable.Creator<Merchant> CREATOR = new Parcelable.Creator<Merchant>() {
        @Override
        public Merchant createFromParcel(Parcel source) {
            return new Merchant(source);
        }

        @Override
        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };
}
