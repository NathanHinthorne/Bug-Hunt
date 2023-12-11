package com.Butterfly.model.cards;

import java.util.List;
import java.util.Map;

public class Dragonfly extends Card {
    public Dragonfly(CardFamily theFamily, CardType theType) {
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
        return "DD";
    }

    @Override
    public int getPoints(Map<CardFamily, List<Card>> playerCollection) {
        int points = 0;

        List<Card> dragonflies = playerCollection.get(myFamily);

        int maxPoints = 0;
        Card maxCard = null;
        for (Card card : dragonflies) {
            if (card.myType.valueOf() > maxPoints) {
                maxPoints = card.myType.valueOf();
                maxCard = card;
            }
        }

        if (maxCard == this) { // only count the max dragonfly
            points = maxPoints;
        }

        return points;
    }


}
