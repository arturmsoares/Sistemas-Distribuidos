package br.edu.iftm.tspi.sd.websockets_ex1.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.time.Instant; // Importe o Instant

@Component
public class WebSocketDisconnectListener {

    private final ChatPresenceService presenceService;
    private final SimpMessagingTemplate messagingTemplate; 

    public WebSocketDisconnectListener(ChatPresenceService presenceService,
                                       @Lazy SimpMessagingTemplate messagingTemplate) {
        this.presenceService = presenceService;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        
        String username = presenceService.removerUsuarioPorSessao(sessionId);
        
        if (username != null) {
            messagingTemplate.convertAndSend("/topic/online", presenceService.getUsuariosOnline());

            Mensagem msg = new Mensagem(
                TipoMensagem.SAIR,
                username,
                null,
                username + " saiu",
                Instant.now()
            );
            messagingTemplate.convertAndSend("/topic/public", msg);
        }
    }
}