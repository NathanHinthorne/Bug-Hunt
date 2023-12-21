//package com.Butterfly.controller.network.handlers;
//
//import com.Butterfly.controller.network.messages.GameStateMessage;
//import org.apache.mina.core.service.IoHandlerAdapter;
//import org.apache.mina.core.session.IoSession;
//
//public class GameStateHandler extends IoHandlerAdapter {
//
//    @Override
//    public void messageReceived(IoSession session, Object message) {
//        if (message instanceof GameStateMessage) {
//            GameStateMessage gameStateMessage = (GameStateMessage) message;
//            // Handle received game state message here
//            // This might involve updating the game state on the client side
//        }
//    }
//}