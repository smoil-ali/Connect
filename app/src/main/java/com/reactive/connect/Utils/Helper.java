package com.reactive.connect.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reactive.connect.model.ProfileClass;

import java.lang.reflect.Type;

public class Helper {
    static String IS_LOGIN = "is_login";
    static String USER_INFO = "user_info";


    public static void setLogin(Context context, boolean isLogin){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Constants.LOGIN,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGIN,isLogin);
        editor.apply();
    }

    public static boolean IsLogin(Context context){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Constants.LOGIN,0);
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public static void setUserInfo(Context context,String user){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Constants.USERS,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_INFO,user);
        editor.apply();
    }

    public static String getUserInfo(Context context){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Constants.USERS,0);
        return sharedPreferences.getString(USER_INFO,"");
    }

    public static String fromProfileToString(ProfileClass model){
        if (model == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ProfileClass>() {
        }.getType();
        String json = gson.toJson(model, type);
        return json;
    }

    public static ProfileClass fromStringToProfile(String model){
        if (model == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ProfileClass>() {
        }.getType();
        ProfileClass profileClass = gson.fromJson(model, type);
        return profileClass;
    }

    public static void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
