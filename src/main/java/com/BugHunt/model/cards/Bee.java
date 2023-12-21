package com.BugHunt.model.cards;

import java.util.ArrayList;
import java.util.Map;

public class Bee extends Card {

    private boolean isPaired;
    public final int PAIRED_VALUE = 0;

    public Bee(CardFamily theFamily, CardType theType) {
        myFamily = theFamily;
        myType = theType;
        isEmpty = false;
        isPaired = false;
        myImagePath = findImagePath();
    }

    // @Override
    // public String findImagePath() {
    // return "/images/error.png";
    // }

    @Override
    public String toString() {
        return "zz";
    }

    @Override
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        int points = 0;

        // search for an unpaired honeycomb within the collection, then pair it. Bee = 0
        // points
        // Otherwise, leave this bee unpaired. Bee = -3 points

        if (isPaired) {
            points = PAIRED_VALUE;

        } else {
            ArrayList<Card> honeycombs = playerCollection.get(CardFamily.HONEYCOMB);

            for (Card card : honeycombs) {
                Honeycomb honeycomb = (Honeycomb) card; // cast it first so we can use its methods

                if (!honeycomb.isPaired()) {
                    honeycomb.pair();
                    this.pair();
                    points = PAIRED_VALUE;
                    // should we update the honeycomb's points here too? if we don't, the
                    // honeycomb's getPoints() method
                    // will need to be called again at some point
                    break;
                }
            }
            if (!isPaired) {
                points = this.getType().valueOf(); // -3 points
            }
        }

        myPoints = points;
    }

    public boolean isPaired() {
        return isPaired;
    }

    public void pair() {
        isPaired = true;
    }

}
