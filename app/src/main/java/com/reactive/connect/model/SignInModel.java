package com.reactive.connect.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import com.reactive.connect.BR;

import java.io.Serializable;

public class SignInModel extends BaseObservable implements Serializable {
    String userId;
    String password;
    boolean displayError;


    public SignInModel() {
    }

    @Bindable
    public String getUserId() {
        if (userId == null)
            return "";
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
    }

    @Bindable({"displayError","userId"})
    public String getUserIdError(){
        if (!isDisplayError()){
            return "";
        }
        if (getUserId().isEmpty()){
            return "User Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getPassword() {
        if (password == null)
            return "";
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable({"displayError","password"})
    public String getPasswordError(){
        if (!isDisplayError()){
            return "";
        }
        if (getPassword().isEmpty()){
            return "Password Field is Empty";
        }
        return "";
    }

    @Bindable
    public boolean isDisplayError(){
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

}
