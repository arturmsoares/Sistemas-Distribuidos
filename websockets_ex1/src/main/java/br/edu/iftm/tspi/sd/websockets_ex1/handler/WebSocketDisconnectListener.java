package br.edu.iftm.tspi.sd.websockets_ex1.handler;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketDisconnectListener {

    private final ChatPresenceService presenceService;

    public WebSocketDisconnectListener(ChatPresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        // o serviço de presença trata a remoção do usuário
        presenceService.removerUsuarioPorSessao(sessionId);
    }
    
}
