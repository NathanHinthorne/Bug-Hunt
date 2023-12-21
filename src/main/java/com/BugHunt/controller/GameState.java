package com.BugHunt.controller;

public enum GameState {
    WAITING_FOR_PLAYERS,
    GAME_STARTING,
    PLAYER_TURN,
    PROCESSING_MOVE,
    GAME_UPDATING,
    GAME_OVER
}