package br.edu.iftm.tspi.sd.websockets_ex1.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;

@Component
public class WebSocketSubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    private final ChatPresenceService presenceService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketSubscribeListener(ChatPresenceService presenceService,
                                      @Lazy SimpMessagingTemplate messagingTemplate) {
        this.presenceService = presenceService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();

        if (Objects.equals(destination, "/topic/online")) {
            var onlineUsers = presenceService.getUsuariosOnline();
            
            messagingTemplate.convertAndSendToUser(
                sessionId,
                "/topic/online",
                onlineUsers,
                accessor.getMessageHeaders()
            );
        }
    }
}