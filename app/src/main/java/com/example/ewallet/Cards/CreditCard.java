package com.example.ewallet.Cards;

import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class CreditCard extends Card {

    //instance variables
    //private String bank;
    private List<Object[]> rewards;
    private int cashBack;
    private boolean isPlace;

    //pre: s != null
    public CreditCard(String s) {
        super(s);
    }

    //pre: s != null
    /*public void setBank(String s) {
        if (s == null) {
            throw new IllegalArgumentException("CreditCard/setBank: parameter cannot be null");
        }
        bank = s;
    }

    public String getBank() {
        return bank;
    }*/

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

    public void setRewards(List<Object[]> object) {
        /*//precondition
        for (int i = 0; i < 4; i++) {
            if (object[i] == null) {
                throw new IllegalArgumentException("CreditCard/setRewards: all elements in object cannot be null");
            }
        }*/
        rewards = object;
    }

    //pre: none
    public List<Object[]> getRewards() {
        return rewards;
    }

    public void setPlace(boolean condition) {
        isPlace = condition;
    }

    public boolean getPlace() {
        return isPlace;
    }
}
