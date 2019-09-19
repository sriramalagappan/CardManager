package com.example.ewallet.Cards;

import android.util.Log;

public class OtherCards extends Card{

    //instance variables
    private String location;
    private int value;
    private int cashBack;

    public OtherCards(String s) {
        super(s);
    }

    public void setLocation(String s) {
        if (s == null) {
            throw new IllegalArgumentException("OtherCards/setLocation: parameter cannot be null");
        }
        location = s;
    }

    public String getLocation() {
        return location;
    }

    public void setValue(int val) {
        value = val;
    }

    public Integer getValue() {
        return value;
    }

    //pre: 0 <= percent <= 100
    public void setCashBack(int percent) {
        if ((percent < 0) || (percent > 100)) {
            throw new IllegalArgumentException("CreditCard/setCashBack: parameter must be between 0 and 100");
        }
        cashBack = percent;
    }

    //pre: none
    public int getCashBack() {
        return cashBack;
    }
}
