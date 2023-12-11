package com.Butterfly.model.cards;

import java.util.List;
import java.util.Map;

public class EmptyCard extends Card {

    public EmptyCard() {
        isEmpty = true;
        myImagePath = findImagePath();
    }

    @Override
    public String findImagePath() {
        return "/images/empty.png";
    }

    @Override
    public int getPoints(Map<CardFamily, List<Card>> playerCollection) {
        return 0;
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
