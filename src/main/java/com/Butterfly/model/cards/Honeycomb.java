package com.Butterfly.model.cards;

import java.util.ArrayList;
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
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        int points = 0;

        // search for an unpaired bee within the collection, then pair it. Honeycomb = regular points
        // Otherwise, leave this honeycomb unpaired. Honeycomb = 0 points

        if (isPaired) {
            points = this.getType().valueOf();

        } else {
            ArrayList<Card> bees = playerCollection.get(CardFamily.BEE);

            for (Card card : bees) {
                Bee bee = (Bee) card; // cast it first so we can use its methods

                if (!bee.isPaired()) {
                    bee.pair();
                    this.pair();
                    points = this.getType().valueOf();
                    // should we update the bee's points here too? if we don't, the bee's getPoints() method
                    // will need to be called again at some point
                    break;
                }
            }
            if (!isPaired) {
                points = UNPAIRED_VALUE; // 0 points
            }
        }

        myPoints = points;
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
