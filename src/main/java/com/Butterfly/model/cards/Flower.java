package com.Butterfly.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Flower extends Card {


    public Flower(CardFamily theFamily, CardType theType) {
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
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        int points = 0;

        List<Card> flowers = playerCollection.get(myFamily);

        // iterate until you find this instance in the list
        for (int i = 0; i < flowers.size(); i++) {
            if (flowers.get(i) == this) {
                points = (int) Math.pow(2, i);
                break;
            }
        }

        myPoints = points;
    }

    @Override
    public String toString() {
        return "FL";
    }
}
