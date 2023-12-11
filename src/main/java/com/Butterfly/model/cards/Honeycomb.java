package com.Butterfly.model.cards;

import java.util.List;
import java.util.Map;

public class Honeycomb extends Card {
    private boolean isPaired;
    public final int UNPAIRED_VALUE = 0;

    public Honeycomb(CardFamily theFamily, CardType theType) {
        myFamily = theFamily;
        myType = theType;
        isEmpty = false;
        isPaired = false;
        myImagePath = findImagePath();
    }

//    @Override
//    public String findImagePath() {
//        return "/images/error.png";
//    }

    @Override
    public int getPoints(Map<CardFamily, List<Card>> playerCollection) {
        int points = 0;

        // search for an unpaired bee within the collection, then pair it. Honeycomb = regular points
        // Otherwise, leave this honeycomb unpaired. Honeycomb = 0 points

        List<Card> bees = playerCollection.get(CardFamily.BEE);

        if (isPaired) {
            points = this.getType().valueOf();

        } else {
            for (Card card : bees) {
                Honeycomb bee = (Honeycomb) card; // cast it first so we can use its methods

                if (!bee.isPaired()) {
                    bee.pair();
                    this.pair();
                    points = this.getType().valueOf();
                    break;
                }
            }
            if (!isPaired) {
                points = UNPAIRED_VALUE;
            }
        }

        return points;
    }

    @Override
    public String toString() {
        return "HH";
    }

    public boolean isPaired() {
        return isPaired;
    }

    public void pair() {
        isPaired = true;
    }
}
