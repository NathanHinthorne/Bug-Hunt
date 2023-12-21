//package com.Butterfly.controller.network.handlers;
//
//import com.Butterfly.controller.network.messages.PlayerActionMessage;
//import org.apache.mina.core.service.IoHandlerAdapter;
//import org.apache.mina.core.session.IoSession;
//
//public class PlayerActionHandler extends IoHandlerAdapter {
//
//    @Override
//    public void messageReceived(IoSession session, Object message) {
//        if (message instanceof PlayerActionMessage) {
//            PlayerActionMessage playerActionMessage = (PlayerActionMessage) message;
//            // Handle received player action message here
//            // This might involve updating the game state on the server side
//        }
//    }
//}