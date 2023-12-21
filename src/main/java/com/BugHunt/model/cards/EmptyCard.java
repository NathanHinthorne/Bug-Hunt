package com.BugHunt.model.cards;

import java.util.ArrayList;
import java.util.Map;

public class EmptyCard extends Card {

    public EmptyCard() {
        isEmpty = true;
        myImagePath = findImagePath();
    }

    @Override
    public String findImagePath() {
        return "/images/cards/empty.png";
    }

    @Override
    public void calculatePoints(Map<CardFamily, ArrayList<Card>> playerCollection) {
        return;
    }

    @Override
    public CardFamily getFamily() {
        return null;
    }

    @Override
    public String toString() {
        return "░░";
    }

}
