package com.reactive.connect.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import com.reactive.connect.BR;

import java.io.Serializable;

public class InterestClass extends BaseObservable implements Serializable {
    String interest;
    boolean checked;

    public InterestClass() {
    }

    @Bindable
    public String getInterest() {
        if (interest == null)
            return "";
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
        notifyPropertyChanged(BR.interest);
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
    }
}
