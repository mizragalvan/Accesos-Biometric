package mx.pagos.admc.socket.stomp;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.session.web.socket.handler.WebSocketRegistryListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class SocketController {

	
	
	@MessageMapping("/req")
	@SubscribeMapping("/resp")
	@SendTo("/topic/resp")
	public RespSocket greeting(ReqSocket message, @Header("simpSessionId") String sessionId, @Header("X-Authorization") String auth, Principal user) throws Exception {

		return new RespSocket("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}

	@MessageMapping("/notifications")
	@SendTo("/topic/resp/notifications")
	public void getNotifications() throws Exception {

	}

}