package com.example.ewallet.Cards;

public class DebitCard extends Card {

    //Instance Variables
    //private String bank;
    private int cashBack;

    public DebitCard(String s) {
        super(s);
    }

    //pre: s != null
    /*public void setBank(String s) {
        if (s == null) {
            throw new IllegalArgumentException("DebitCard/setBank: parameter cannot be null");
        }
        //bank = s;
    }

    public String getBank() {
        return bank;
    }*/

    //pre: 0 <= percent <= 100
    public void setCashBack(int percent) {
        if ((percent < 0) || (percent > 100)) {
            throw new IllegalArgumentException("DebitCard/setCashBack: parameter must be between 0 and 100");
        }
        cashBack = percent;
    }

    //pre: none
    public int getCashBack() {
        return cashBack;
    }
}
