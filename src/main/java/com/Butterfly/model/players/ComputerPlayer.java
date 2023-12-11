package com.Butterfly.model.players;

import com.Butterfly.model.board.GlobalDir;

public class ComputerPlayer extends Player {

    @Override
    public GlobalDir chooseDirection() {
        System.out.println(myName + "'s turn.");
        return null;
    }

    @Override
    public int chooseSpaces() {
        return 0;
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
