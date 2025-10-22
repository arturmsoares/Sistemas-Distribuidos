package br.edu.iftm.tspi.sd.websockets_ex1.handler;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private static final Set<String> usuariosOnline = Collections.synchronizedSet(new HashSet<>());

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Mensagem enviarTexto(Mensagem mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.ENVIAR_TEXTO);
        mensagem.setDataHora(Instant.now());
        return mensagem;
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public Mensagem entrar(Mensagem mensagem) {

        usuariosOnline.add(mensagem.getOrigem());
        messagingTemplate.convertAndSend("/topic/online", usuariosOnline);

        mensagem.setTipoMensagem(TipoMensagem.ENTRAR);
        mensagem.setDataHora(Instant.now());
        mensagem.setTexto(mensagem.getOrigem() + " entrou");
        return mensagem;
    }

    @MessageMapping("/chat.leave")
    @SendTo("/topic/public")
    public Mensagem sair(Mensagem mensagem) {

        usuariosOnline.remove(mensagem.getOrigem());
        messagingTemplate.convertAndSend("/topic/online", usuariosOnline);

        mensagem.setTipoMensagem(TipoMensagem.SAIR);
        mensagem.setDataHora(Instant.now());
        mensagem.setTexto(mensagem.getOrigem() + " saiu");
        return mensagem;
    }
}
