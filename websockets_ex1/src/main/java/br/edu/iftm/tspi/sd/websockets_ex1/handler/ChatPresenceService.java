package br.edu.iftm.tspi.sd.websockets_ex1.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatPresenceService {

    // para para rastrar as seções para os usuários
    private final Map<String, String> sessaoParaUsuario = new ConcurrentHashMap<>();
    
    // set para a lista ública de usuários online
    private final Set<String> usuariosOnline = Collections.synchronizedSet(new HashSet<>());

    
    private final SimpMessagingTemplate messagingTemplate;

    public ChatPresenceService(@Lazy SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    //chamado quando um usuário se conecta
    public void adicionarUsuario(String sesssionId, String username){
        if(username==null) return;


        //acrescenta no MAP de determinada sessão o usuário
        sessaoParaUsuario.put(sesssionId, username);
        //acrescenta um nome no SET de usuários online
        usuariosOnline.add(username);

        //para notificar o /topic/online
        enviarListaUsuariosOnline();

        //para notificar o /topic/public
        Mensagem msg = new Mensagem(TipoMensagem.ENTRAR, username, null, username + " entrou", Instant.now());
        messagingTemplate.convertAndSend("/topic/public", msg);
    }

    public void removerUsuarioPorNome(String username){
        if(username == null) return;

        String sessionId= null;
        for(Map.Entry<String, String> entry : sessaoParaUsuario.entrySet()){
            if(entry.getValue().equals(username)){
                sessionId = entry.getKey();
                break;
            }
        }

        if (sessionId != null){
            sessaoParaUsuario.remove(sessionId);
        }
        usuariosOnline.remove(username);

        Mensagem msg = new Mensagem(TipoMensagem.SAIR, username, null, username + " saiu", Instant.now());
        messagingTemplate.convertAndSend("/topic/public", msg);
        
    }

    public String removerUsuarioPorSessao (String sessionId){
        String username = sessaoParaUsuario.remove(sessionId);

        if (username != null){
            usuariosOnline.remove(username);

            enviarListaUsuariosOnline();
            Mensagem msg = new Mensagem(TipoMensagem.SAIR, username, null, username + " saiu", Instant.now());
            messagingTemplate.convertAndSend("/topic/public", msg);
        }
        return username;
    }

    private void enviarListaUsuariosOnline() {
        messagingTemplate.convertAndSend("/topic/presenca", usuariosOnline);
    }

    

    
}