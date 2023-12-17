package com.Butterfly.model.players;

import com.Butterfly.model.cards.*;

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
