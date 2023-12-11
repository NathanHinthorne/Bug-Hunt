package com.Butterfly.model.cards;

public enum CardFamily {
    RED_BUTTERFLY("RedButterfly"),
    BLUE_BUTTERFLY("BlueButterfly"),
    YELLOW_BUTTERFLY("YellowButterfly"),
    GREEN_BUTTERFLY("GreenButterfly"),
    GRASSHOPPER("Grasshopper"),
    FIREFLY("Firefly"),
    DRAGONFLY("Dragonfly"),
    FLOWER("Flower"),
    HONEYCOMB("Honeycomb"),
    BEE("Bee"),
    WASP("Wasp");


    private final String myFamily;
    CardFamily(String theFamily) {
        myFamily = theFamily;
    }

    @Override
    public String toString() {
        return myFamily;
    }
}
