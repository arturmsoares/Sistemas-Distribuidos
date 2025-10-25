package br.edu.iftm.tspi.sd.websockets_ex1.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PresenceChannelInterceptor implements ChannelInterceptor {

    private final ChatPresenceService presenceService;

    private final ObjectMapper objectMapper; // deserializador JSON do payload

    //payLoad é o conteudo da mensagem

    public PresenceChannelInterceptor(ChatPresenceService presenceService, ObjectMapper objectMapper) {
        this.presenceService = presenceService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Message<?>preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.SEND.equals(accessor.getCommand())){
            String destination = accessor.getDestination();
            String sessionId = accessor.getSessionId();

            try {

                Mensagem msg = objectMapper.readValue((byte[])message.getPayload(), Mensagem.class);
                String username = msg.getOrigem();


                if("/app/chat.join".equals(destination)){
                    presenceService.adicionarUsuario(sessionId, username);
                    return null; //cancela o envio da msg para o ChatController
                }

                if ("/app/chat.leave".equals(destination)) {
                    presenceService.removerUsuarioPorNome(username);
                    return null; 
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return message;
    }
   
}
