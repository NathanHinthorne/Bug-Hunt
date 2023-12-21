package com.BugHunt.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Firefly extends Card {
    public Firefly(CardFamily theFamily, CardType theType) {
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
        return "FF";
    }

    @Override
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        int points;

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
        } else {
            points = 0;
        }

        myPoints = points;
    }

}
