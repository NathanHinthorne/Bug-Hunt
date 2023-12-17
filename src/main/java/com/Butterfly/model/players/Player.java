package com.Butterfly.model.players;

import com.Butterfly.model.cards.*;

import java.util.*;

public abstract class Player implements java.io.Serializable {

    protected String myName;
    private int myTotalCards;
    private int myScore;

    /**
     * A map of the player's cards, organized by family
     */
    Map<CardFamily, ArrayList<Card>> organizedCards = new TreeMap<>();

    /**
     * A set of the player's cards, not organized
     */
    private Set<Card> disorganizedCards;

    public Player() {
        myTotalCards = 0;
        myScore = 0;
        disorganizedCards = new HashSet<>();

        for (CardFamily family : CardFamily.values()) {
            organizedCards.put(family, new ArrayList<>());
        }
    }

    public abstract Card cardToMoveTo();

    public abstract boolean isHuman();

    public void setName(String name) {
        myName = name;
    }

    public String getName() {
        return myName;
    }

    public void collectCard(Card card) {

        organizedCards.get(card.getFamily()).add(card); // group cards according to their family
        disorganizedCards.add(card); // add card to the disorganized set

        // only add points AFTER collecting card (this is important in the getPoints()
        // method)
        myScore = 0;

        for (Card cardInGroup : disorganizedCards) {
            cardInGroup.calculatePoints(organizedCards); // this doesn't modify score, just updates the points of each
                                                         // card
            myScore += cardInGroup.getPoints(); // this is where the score is actually updated
        }

        myTotalCards++;
    }

    public Map<CardFamily, ArrayList<Card>> getOrganizedCards() {
        return new TreeMap<>(organizedCards); // return a copy, not a reference to the original
    }

    public Set<Card> getDisorganizedCards() {
        return new HashSet<>(disorganizedCards); // return a copy, not a reference to the original
    }

    public int getTotalCards() {
        return myTotalCards;
    }

    public int getScore() {
        return myScore;
    }

    public void addPoints(int points) {
        myScore += points;
    }
}
