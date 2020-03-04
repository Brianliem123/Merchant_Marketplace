package com.fa.marketplace_merchant.Class;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RegisterErrorRespone {
    @SerializedName("email")
    List<String> emailError = new ArrayList<>();

    public List<String> getEmailError() {
        return emailError;
    }
}