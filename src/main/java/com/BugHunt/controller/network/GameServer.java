//package com.Butterfly.controller.network;
//
//import com.Butterfly.controller.GameState;
//import com.Butterfly.controller.network.handlers.GameStateHandler;
//import com.Butterfly.controller.network.messages.GameStateMessage;
//import org.apache.mina.core.service.IoAcceptor;
//import org.apache.mina.core.session.IdleStatus;
//import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//
//public class GameServer {
//    private IoAcceptor acceptor;
//    private GameState gameState;
//
//    public GameServer() throws IOException {
//        acceptor = new NioSocketAcceptor();
//        acceptor.setHandler(new GameStateHandler());
//        acceptor.getSessionConfig().setReadBufferSize(2048);
//        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
//        acceptor.bind(new InetSocketAddress(54555));
//
//        gameState = GameState.WAITING_FOR_PLAYERS;
//    }
//
//    public void start() {
//        gameState = GameState.GAME_STARTING;
//        // Initialize game state here
//        acceptor.broadcast(new GameStateMessage(gameState));
//    }
//
//    public void nextPlayerTurn() {
//        gameState = GameState.PLAYER_TURN;
//        // Start next player's turn
//        acceptor.broadcast(new GameStateMessage(gameState));
//    }
//
//    public void processMove() {
//        gameState = GameState.PROCESSING_MOVE;
//        // Process player's move
//        acceptor.broadcast(new GameStateMessage(gameState));
//    }
//
//    public void updateGameState() {
//        gameState = GameState.GAME_UPDATING;
//        // Update game state
//        acceptor.broadcast(new GameStateMessage(gameState));
//    }
//
//    public void endGame() {
//        gameState = GameState.GAME_OVER;
//        // Handle end of game
//        acceptor.broadcast(new GameStateMessage(gameState));
//    }
//}