package br.edu.iftm.tspi.sd.websockets_exemplo.handler;

import java.time.Instant;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    
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
        mensagem.setTipoMensagem(TipoMensagem.ENTRAR);
        mensagem.setDataHora(Instant.now());
        mensagem.setTexto(mensagem.getOrigem() + " entrou");
        return mensagem;
    }

    @MessageMapping("/chat.leave")
    @SendTo("/topic/public")
    public Mensagem sair(Mensagem mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.SAIR);
        mensagem.setDataHora(Instant.now());
        mensagem.setTexto(mensagem.getOrigem() + " saiu");
        return mensagem;
    }
}
