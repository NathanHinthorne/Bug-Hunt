package com.Butterfly.model.players;

import com.Butterfly.model.board.GlobalDir;

import java.util.Scanner;

public class HumanPlayer extends Player {

    private final Scanner input = new Scanner(System.in);

    public HumanPlayer() {

    }

    @Override
    public GlobalDir chooseDirection() {
        System.out.println(myName + "'s turn.");
        System.out.print("Enter a direction to move (w, a, s, d): ");

        GlobalDir dir = null;


        switch (input.next()) {
            case "w":
                dir = GlobalDir.NORTH;
                break;
            case "a":
                dir = GlobalDir.WEST;
                break;
            case "s":
                dir = GlobalDir.SOUTH;
                break;
            case "d":
                dir = GlobalDir.EAST;
                break;
            default:
                System.out.println("Invalid input. Try again.");
                chooseDirection();
        }

        return dir;
    }

    @Override
    public int chooseSpaces() {
        System.out.print("Enter a number of spaces to move: ");
        return input.nextInt();
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
