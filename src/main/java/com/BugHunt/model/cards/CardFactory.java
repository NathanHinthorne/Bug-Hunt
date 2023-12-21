package com.BugHunt.model.cards;

public class CardFactory {
    private static Card card;

    public static Card createCard() {
        return new EmptyCard();
    }

    public static Card createCard(CardFamily theFamily, CardType theType) {

        if (theFamily == null || theType == null) {
            throw new IllegalArgumentException("Invalid card family or type: " + theFamily + " " + theType);
        }

        switch (theFamily) {
            case RED_BUTTERFLY:
            case BLUE_BUTTERFLY:
            case YELLOW_BUTTERFLY:
            case GREEN_BUTTERFLY:
                card = new Butterfly(theFamily, theType);
                break;

            case GRASSHOPPER:
                card = new Grasshopper(theFamily, theType);
                break;

            case FIREFLY:
                card = new Firefly(theFamily, theType);
                break;

            case DRAGONFLY:
                card = new Dragonfly(theFamily, theType);
                break;

            case FLOWER:
                card = new Flower(theFamily, theType);
                break;

            case HONEYCOMB:
                card = new Honeycomb(theFamily, theType);
                break;

            case BEE:
                card = new Bee(theFamily, theType);
                break;

            case WASP:
                card = new Wasp(theFamily, theType);
                break;

            default:
                throw new IllegalArgumentException("Invalid card family: " + theFamily);
        }

        return card;
    }
}
