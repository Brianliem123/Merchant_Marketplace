package com.fa.marketplace_merchant.Utils;

import android.content.SharedPreferences;

import com.fa.marketplace_merchant.Model.AccessToken;

public class TokenManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static TokenManager INSTANCE;

    public TokenManager(SharedPreferences pref){
        this.sharedPreferences = pref;
        this.editor = sharedPreferences.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences pref){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(pref);
        }

        return INSTANCE;
    }

    public void saveToken(AccessToken accessToken){
        editor.putString("TOKEN_TYPE",accessToken.getTokenType()).commit();
        editor.putString("ACCESS_TOKEN",accessToken.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN",accessToken.getRefreshToken()).commit();

    }

    public void deleteToken(){
        editor.remove("TOKEN_TYPE").commit();
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();

    }

    public AccessToken getToken(){
        AccessToken accessTokem = new AccessToken();
        accessTokem.setTokenType(sharedPreferences.getString("TOKEN_TYPE",null));
        accessTokem.setAccessToken(sharedPreferences.getString("ACCESS_TOKEN",null));
        accessTokem.setRefreshToken(sharedPreferences.getString("REFRESH_TOKEN",null));
        return accessTokem;
    }

}