package com.Butterfly.model.cards;

import java.util.ArrayList;
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



    // two ways to look at getPoints()

        // 1. we only call getPoints() once every time we collect a card
            // * more efficient
            // * weird for a get method to change the state of the object
            // * makes sense given the fact we're ALREADY receiving the player's collection as a parameter

        // (current approach)
        // 2. we call getPoints() on every card when we need to update the points (i.e. every time we add a card to the collection)
            // * less efficient
            // * more intuitive

    @Override
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        int points;

        ArrayList<Card> grasshoppers = playerCollection.get(myFamily);

        Card lastGrasshopper = grasshoppers.get(grasshoppers.size()-1);

        if (lastGrasshopper == this) { // only count the last grasshopper
            points = this.getType().valueOf();
        } else {
            points = 0;
        }

        myPoints = points;
    }

}
