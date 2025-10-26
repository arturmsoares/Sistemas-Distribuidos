package br.edu.iftm.tspi.sd.websockets_ex1.handler;

import java.time.Instant;

import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    
    public ChatController(@Lazy SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Mensagem enviarTexto(Mensagem mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.ENVIAR_TEXTO);
        mensagem.setDataHora(Instant.now());
        return mensagem;
    }

    @MessageMapping("/chat.private")
    public void enviarPrivado(Mensagem mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.PRIVADO);
        mensagem.setDataHora(Instant.now());

        String destTopic = "/topic/dm." + mensagem.getDestino();
        
        messagingTemplate.convertAndSend(destTopic, mensagem);

        String senderTopic = "/topic/dm." + mensagem.getOrigem();
        messagingTemplate.convertAndSend(senderTopic, mensagem);
    }

    
}
