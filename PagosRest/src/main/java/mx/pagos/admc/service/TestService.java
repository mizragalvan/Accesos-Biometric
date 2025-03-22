package mx.pagos.admc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.pagos.admc.socket.stomp.ReqSocket;
import mx.pagos.admc.socket.stomp.RespSocket;
import mx.pagos.security.structures.UserSession;

@RestController
public class TestService {

    
	@Autowired
	private SessionRepository<? extends Session> webSession;
	
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SimpUserRegistry simpUserRegistry;
    

    @PostMapping (value = "testStomp.do")
    public final void test(@RequestBody ReqSocket message) {
    	
    	   String sessionId=message.getName();
    	   
    	   UserSession ses=webSession.findById(sessionId).getAttribute("scopedTarget.UserSession");
    	
    	   
    	   
    	   this.template.convertAndSendToUser(sessionId,"/topic/resp", new RespSocket("Hola vengo del Server Privado, !"));    
    }
    
    @PostMapping (value = "testStomp2.do")
    public final void test() {      	
    	
    	   this.template.convertAndSend("/topic/resp", new RespSocket("Hola vengo del Server, !"));   
    }
    
    public static void main(String[] args) throws JsonProcessingException {
		
    	ObjectMapper mapper = new ObjectMapper();
    	ReqSocket message =new ReqSocket();
    	
    	message.setName("fsadfasdf");
    	System.out.println(mapper.writeValueAsString(message));
    	
	}
  
}
