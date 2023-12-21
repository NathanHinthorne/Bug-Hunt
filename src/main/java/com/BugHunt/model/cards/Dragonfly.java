package com.BugHunt.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dragonfly extends Card {
    public Dragonfly(CardFamily theFamily, CardType theType) {
        myFamily = theFamily;
        myType = theType;
        isEmpty = false;
        myImagePath = findImagePath();
    }

    @Override
    public String toString() {
        return "DD";
    }

    @Override
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        int points;

        List<Card> dragonflies = playerCollection.get(myFamily);

        int maxPoints = 0;
        Card maxCard = null;
        for (Card card : dragonflies) {
            if (card.myType.valueOf() > maxPoints) {
                maxCard = card;
                maxPoints = card.myType.valueOf();
            }
        }

        if (maxCard == this) { // only count the max dragonfly
            points = this.myType.valueOf();
        } else {
            points = 0;
        }

        myPoints = points;
    }

}
