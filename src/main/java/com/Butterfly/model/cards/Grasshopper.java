package com.Butterfly.model.cards;

import java.util.List;
import java.util.Map;

public class Grasshopper extends Card {

    public Grasshopper(CardFamily theFamily, CardType theType) {
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
        return "GG";
    }

    @Override
    public int getPoints(Map<CardFamily, List<Card>> playerCollection) {
        int points = 0;

        List<Card> grasshoppers = playerCollection.get(myFamily);

        Card lastGrasshopper = grasshoppers.get(grasshoppers.size()-1);

        if (lastGrasshopper == this) { // only count the last grasshopper
            points = this.getType().valueOf();
        }

        return points;
    }

}
