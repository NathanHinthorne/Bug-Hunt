package com.Butterfly.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Butterfly extends Card {

    public Butterfly(CardFamily theFamily, CardType theType) {
        myFamily = theFamily;
        myType = theType;
        isEmpty = false;
        myImagePath = findImagePath();
    }

    @Override
    public String toString() {
        String card = "";

        if (myFamily == CardFamily.RED_BUTTERFLY) {
            card = "BR";
        } else if (myFamily == CardFamily.BLUE_BUTTERFLY) {
            card = "BB";
        } else if (myFamily == CardFamily.YELLOW_BUTTERFLY) {
            card = "BY";
        } else if (myFamily == CardFamily.GREEN_BUTTERFLY) {
            card = "BG";
        }

        return card;
    }

    @Override
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        int points = 0;

        List<Card> butterflies = playerCollection.get(myFamily); // get all butterflies of this color

        boolean hasDouble = false; // check if player has a double butterfly of this color

        // run through the list once to determine if there's a double
        for (Card card : butterflies) {
            if (card.myType == CardType.DOUBLE) {
                hasDouble = true;
                break;
            }
        }

        points = this.myType.valueOf(); // get the value of the butterfly

        if (hasDouble) {
            points *= 2;
        }

        myPoints = points;
    }

}
