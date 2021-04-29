package com.reactive.connect.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.connect.BR;
import com.reactive.connect.Utils.Constants;

import java.io.Serializable;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

public class ProfileClass extends BaseObservable implements Serializable {

    final String TAG = ProfileClass.class.getSimpleName();
    String id;
    String fullName;
    String password;
    String image;
    String bio;
    List<String> interest;
    String phone;
    String email;
    String gender;
    boolean displayError;
    boolean isUserExist;
    boolean allow = true;


    public ProfileClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getFullName() {
        if (fullName == null)
            return "";
        return fullName.toLowerCase();
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        notifyPropertyChanged(BR.fullName);
    }

    @Bindable({"displayError","fullName"})
    public String getFullNameError(){
        if (!isDisplayError()){
            return "";
        }

        if (getFullName().isEmpty()){
            return "Full Name Field is Empty";
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
    public String getImage() {
        if (image == null)
            return "";
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }


    @Bindable
    public String getBio() {
        if (bio == null)
            return "";
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
        notifyPropertyChanged(BR.bio);
    }

    @Bindable({"displayError","bio"})
    public String getBioError(){
        if (!isDisplayError()){
            return "";
        }

        if (getBio().isEmpty()){
            return "Bio Field is Empty";
        }
        return "";
    }

    @Bindable
    public List<String> getInterest() {
        if (interest == null)
            return new ArrayList<>();
        return interest;
    }

    public void setInterest(List<String> interest) {
        this.interest = interest;
        notifyPropertyChanged(BR.interest);
    }

    @Bindable({"displayError","interest"})
    public String getInterestError(){
        if (!isDisplayError()){
            return "";
        }

        if (getInterest().isEmpty()){
            return "Interest Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getPhone() {
        if (phone == null)
            return "";
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable({"displayError","phone"})
    public String getPhoneError(){
        if (!isDisplayError()){
            return "";
        }

        if (getPhone().isEmpty()){
            return "Phone Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getEmail() {
        if (email == null)
            return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable({"displayError","email"})
    public String getEmailError(){
        if (!isDisplayError()){
            return "";
        }

        if (getEmail().isEmpty()){
            return "Email Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getGender() {
        if (gender == null)
            return "";
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }


    @Bindable
    public boolean isDisplayError(){
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    @Override
    public String toString() {
        return "ProfileClass{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", bio='" + bio + '\'' +
                ", interest=" + interest +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", displayError=" + displayError +
                '}';
    }
}
