package com.Butterfly.model.players;

import com.Butterfly.model.board.GlobalDir;
import com.Butterfly.model.cards.Card;
import com.Butterfly.model.cards.CardFamily;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class Player {

    protected String myName;
    Map<CardFamily, List<Card>> cardCollection = new TreeMap<>();
    private int myTotalCards;
    private int myPoints;

    public Player() {
        myTotalCards = 0;
        myPoints = 0;

        for (CardFamily family : CardFamily.values()) {
            cardCollection.put(family, new ArrayList<>());
        }
    }


    public abstract GlobalDir chooseDirection();
    public abstract int chooseSpaces();

    public abstract boolean isHuman();

    public void setName(String name) {
        myName = name;
    }

    public String getName() {
        return myName;
    }

    public void collectCard(Card card) {
        //cast card to its family before letting player collect it

        cardCollection.get(card.getFamily()).add(card); // group cards according to their family
        myTotalCards++;
    }

    public Map<CardFamily, List<Card>> getCollection() {
        return new TreeMap<>(cardCollection); // return a copy, not a reference to the original
    }

    public void printTextCollection() {
        System.out.println(myName + "'s collection:\n");
        for (CardFamily family : CardFamily.values()) {
            List<Card> cardGroup = cardCollection.get(family);

            if (!cardGroup.isEmpty()) {
                System.out.print(family + " - ");
                for (Card card : cardGroup) {
                    System.out.print(card.getType() + ", ");
                }
                System.out.println();
            }
        }
        System.out.print("\n\n");
    }

    public void displayCollection() {
        System.out.println(myName + "'s collection:\n");
        for (CardFamily family : CardFamily.values()) {
            List<Card> cardGroup = cardCollection.get(family);

            if (!cardGroup.isEmpty()) {
                System.out.print(family + " - ");
                for (Card card : cardGroup) {
                    System.out.print(card.getType() + ", ");
                }
                System.out.println();
            }
        }
        System.out.print("\n\n");
    }

    public int getTotalCards() {
        return myTotalCards;
    }

    public int getScore() {
        return myPoints;
    }
}
