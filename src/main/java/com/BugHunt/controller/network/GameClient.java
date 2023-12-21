//package com.Butterfly.controller.network;
//
//import com.Butterfly.controller.network.handlers.PlayerActionHandler;
//import com.Butterfly.controller.network.messages.PlayerActionMessage;
//import org.apache.mina.core.future.ConnectFuture;
//import org.apache.mina.transport.socket.nio.NioSocketConnector;
//
//import java.net.InetSocketAddress;
//
//public class GameClient {
//    private NioSocketConnector connector;
//
//    public GameClient() {
//        connector = new NioSocketConnector();
//        connector.setHandler(new PlayerActionHandler());
//    }
//
//    public void connect() {
//        ConnectFuture future = connector.connect(new InetSocketAddress("localhost", 54555));
//        future.awaitUninterruptibly();
//    }
//
//    public void sendPlayerAction(String action) {
//        connector.getManagedSessions().values().iterator().next().write(new PlayerActionMessage(action));
//    }
//}