package br.edu.iftm.tspi.sd.websockets_ex1.handler;

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

    // (sessionId -> username) Mapa para rastrear sessões
    private final Map<String, String> sessaoParaUsuario = new ConcurrentHashMap<>();
    
    // (username) Set para a lista pública de usuários
    private final Set<String> usuariosOnline = Collections.synchronizedSet(new HashSet<>());

    private final SimpMessagingTemplate messagingTemplate;

    public ChatPresenceService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Chamado quando um usuário se conecta (via Interceptor).
     */
    public void adicionarUsuario(String sessionId, String username) {
        if (username == null) return;
        
        sessaoParaUsuario.put(sessionId, username);
        usuariosOnline.add(username);

        // Notifica o /topic/online
        enviarListaUsuariosOnline();
        
        // Notifica o /topic/public
        Mensagem msg = new Mensagem(TipoMensagem.ENTRAR, username, null, username + " entrou", Instant.now());
        messagingTemplate.convertAndSend("/topic/public", msg);
    }

    /**
     * Chamado quando um usuário desconecta limpa (via Interceptor).
     */
    public void removerUsuarioPorNome(String username) {
        if (username == null) return;

        // Encontra o sessionId pelo username
        String sessionId = null;
        for (Map.Entry<String, String> entry : sessaoParaUsuario.entrySet()) {
            if (entry.getValue().equals(username)) {
                sessionId = entry.getKey();
                break;
            }
        }
        
        if (sessionId != null) {
            sessaoParaUsuario.remove(sessionId);
        }
        usuariosOnline.remove(username);

        // Notifica todos
        enviarListaUsuariosOnline();
        Mensagem msg = new Mensagem(TipoMensagem.SAIR, username, null, username + " saiu", Instant.now());
        messagingTemplate.convertAndSend("/topic/public", msg);
    }

    /**
     * Chamado quando uma sessão é desconectada (via EventListener).
     * @return O nome do usuário que foi removido.
     */
    public String removerUsuarioPorSessao(String sessionId) {
        String username = sessaoParaUsuario.remove(sessionId);
        
        if (username != null) {
            usuariosOnline.remove(username);
            
            // Notifica todos
            enviarListaUsuariosOnline();
            Mensagem msg = new Mensagem(TipoMensagem.SAIR, username, null, username + " saiu", Instant.now());
            messagingTemplate.convertAndSend("/topic/public", msg);
        }
        return username;
    }

    private void enviarListaUsuariosOnline() {
        messagingTemplate.convertAndSend("/topic/online", usuariosOnline);
    }
}