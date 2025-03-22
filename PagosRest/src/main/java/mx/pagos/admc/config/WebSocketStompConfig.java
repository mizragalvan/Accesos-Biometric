package mx.pagos.admc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import mx.pagos.admc.socket.stomp.UserSessionInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session>  {
//	public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic"); // Angular Servidor
		config.setApplicationDestinationPrefixes("/app"); // Servidor Angular
		config.setUserDestinationPrefix("/user"); // Usuario especifico
	}

//	@Override
//	public void registerStompEndpoints(StompEndpointRegistry registry) {
//		registry.addEndpoint("/csws")
//		        .setAllowedOrigins("*")
//		        .withSockJS();
//	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new UserSessionInterceptor());
	}

	@Override
	protected void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/csws")
        .setAllowedOrigins("*")
        .withSockJS();
	}
}
