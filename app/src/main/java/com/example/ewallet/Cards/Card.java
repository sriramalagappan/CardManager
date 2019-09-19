package com.example.ewallet.Cards;

import java.util.List;

public class Card {

    //instance variables
    private String name;

    //pre: s != null
    public Card(String s) {
        //precondition
        if (name == null) {
            //throw new IllegalArgumentException("Card: parameter cannot be null");
        }
        setName(s);
    }

    //pre: s != null
    public void setName(String s) {
        if (name == null) {
            //throw new IllegalArgumentException("Card/SetName: parameter cannot be null");
        }
        name = s;
    }

    //pre: non
    public String getName() {
        return name;
    }
}
