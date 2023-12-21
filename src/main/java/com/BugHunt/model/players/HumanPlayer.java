package com.BugHunt.model.players;

import com.BugHunt.model.cards.*;

public class HumanPlayer extends Player {

    public HumanPlayer() {

    }

    @Override
    public Card cardToMoveTo() {
        return null;
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
