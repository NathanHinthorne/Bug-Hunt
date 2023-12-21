package com.BugHunt.model.cards;

import java.util.ArrayList;
import java.util.Map;

public class Wasp extends Card {
    public Wasp(CardFamily theFamily, CardType theType) {
        myFamily = theFamily;
        myType = theType;
        isEmpty = false;
        myImagePath = findImagePath();
    }

    // @Override
    // public String findImagePath() {
    // return "/images/error.png";
    // }

    @Override
    public String toString() {
        return "ZZ";
    }

    @Override
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        myPoints = this.myType.valueOf(); // pretty simple
    }

}
