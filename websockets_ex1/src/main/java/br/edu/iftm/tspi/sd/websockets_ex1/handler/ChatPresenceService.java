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

    

    public ChatPresenceService(){}

    //chamado quando um usuário se conecta
    public void adicionarUsuario(String sesssionId, String username){
        if(username==null) return;
        //acrescenta no MAP de determinada sessão o usuário
        sessaoParaUsuario.put(sesssionId, username);
        //acrescenta um nome no SET de usuários online
        usuariosOnline.add(username);
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
    }

    public String removerUsuarioPorSessao (String sessionId){
        String username = sessaoParaUsuario.remove(sessionId);

        if (username != null){
            usuariosOnline.remove(username);
        }
        return username;
    }

    public Set<String> getUsuariosOnline(){
        return usuariosOnline;
    }   
}