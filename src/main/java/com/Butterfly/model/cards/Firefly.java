package com.Butterfly.model.cards;

import java.util.List;
import java.util.Map;

public class Firefly extends Card {
    public Firefly(CardFamily theFamily, CardType theType) {
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
        return "FF";
    }

    @Override
    public int getPoints(Map<CardFamily, List<Card>> playerCollection) {
        int points = 0;

        List<Card> fireflies = playerCollection.get(myFamily);

        int minPoints = 100;
        Card minCard = null;
        for (Card card : fireflies) {
            if (card.myType.valueOf() < minPoints) {
                minPoints = card.myType.valueOf();
                minCard = card;
            }
        }

        if (minCard == this) { // only count the min firefly
            points = minPoints;
        }

        return points;
    }

}
