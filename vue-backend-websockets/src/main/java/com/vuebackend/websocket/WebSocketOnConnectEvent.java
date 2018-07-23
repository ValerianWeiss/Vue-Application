package com.vuebackend.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketOnConnectEvent implements ApplicationListener<SessionConnectedEvent> {

    private static int activeViewCount;


    public static int getActiveViewCount() {
        return activeViewCount;
    }    

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        System.out.println("A client connected. Total amount of connected clients: "
            + activeViewCount);
    }
}