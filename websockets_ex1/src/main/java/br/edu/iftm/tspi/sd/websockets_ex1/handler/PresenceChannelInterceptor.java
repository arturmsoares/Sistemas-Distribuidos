package br.edu.iftm.tspi.sd.websockets_ex1.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy; 
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant; 

@Component
public class PresenceChannelInterceptor implements ChannelInterceptor {

    private final ChatPresenceService presenceService;
    private final SimpMessagingTemplate messagingTemplate; 
    private final ObjectMapper objectMapper;

    public PresenceChannelInterceptor(ChatPresenceService presenceService,
                                      @Lazy SimpMessagingTemplate messagingTemplate, 
                                      ObjectMapper objectMapper) {
        this.presenceService = presenceService;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.SEND.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            String sessionId = accessor.getSessionId();

            try {
                Mensagem msg = objectMapper.readValue((byte[]) message.getPayload(), Mensagem.class);
                String username = msg.getOrigem();

                if ("/app/chat.join".equals(destination)) {
                    presenceService.adicionarUsuario(sessionId, username);
                    
                    // envia a lista atualizada para /topic/online
                    messagingTemplate.convertAndSend("/topic/online", presenceService.getUsuariosOnline());

                    msg.setTipoMensagem(TipoMensagem.ENTRAR);
                    msg.setDataHora(Instant.now());
                    msg.setTexto(username + " entrou");
                    messagingTemplate.convertAndSend("/topic/public", msg);
                    
                    return null; 
                }

                if ("/app/chat.leave".equals(destination)) {
                    presenceService.removerUsuarioPorNome(username);

                    // Envia a lista atualizada para /topic/online
                    messagingTemplate.convertAndSend("/topic/online", presenceService.getUsuariosOnline());

                    // Envia a mensagem pública de "saiu"
                    msg.setTipoMensagem(TipoMensagem.SAIR);
                    msg.setDataHora(Instant.now());
                    msg.setTexto(username + " saiu");
                    messagingTemplate.convertAndSend("/topic/public", msg);
                    
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message; // retornar a mensagem original se não for manipulada
    }
}