package com.Butterfly.model.cards;

import java.util.List;
import java.util.Map;

public class Wasp extends Card {
    public Wasp(CardFamily theFamily, CardType theType) {
        myFamily = theFamily;
        myType = theType;
        isEmpty = false;
        myImagePath = findImagePath();
    }

//    @Override
//    public String findImagePath() {
//        return "/images/error.png";
//    }

    @Override
    public String toString() {
        return "ZZ";
    }

    @Override
    public int getPoints(Map<CardFamily, List<Card>> playerCollection) {
        return this.myType.valueOf(); // pretty simple
    }

}
