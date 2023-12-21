package com.BugHunt.model.cards;

public enum CardType {
    ZERO(0, "0"), ONE(1, "1"), TWO(2, "2"), THREE(3, "3"),
    FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"), SEVEN(7, "7"),
    EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"), ELEVEN(11, "11"),
    TWELVE(12, "12"), THIRTEEN(13, "13"), FOURTEEN(14, "14"), FIFTEEN(15, "15"),
    NEGATIVE_THREE(-3, "Neg3"), NEGATIVE_FOUR(-4, "Neg4"),
    NEGATIVE_FIVE(-5, "Neg5"), NEGATIVE_SIX(-6, "Neg6"), NEGATIVE_SEVEN(-7, "Neg7"),
    DOUBLE(0, "Double"), SQUARE(0, "Square");

    private final int myValue;
    private final String myFileName;
    CardType(int theValue, String theFileName) {
        myValue = theValue;
        myFileName = theFileName;
    }

    public int valueOf() {
        return myValue;
    }

    @Override
    public String toString() {
        return myFileName;
    }

}
