package com.wkhmedical;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.wkhmedical.message.event.ConnectEvent;
import com.wkhmedical.message.event.ConnectEvent.TYPE;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Resource
	ApplicationContext applicationContext;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		 registry.addEndpoint("/stomp").setAllowedOrigins("*").addInterceptors(new WebSocketHandshakeInterceptor());
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(1);
        te.setThreadNamePrefix("ws-heartbeat-thread-");
        te.initialize();
		
		registry.enableSimpleBroker("/topic", "/queue").setHeartbeatValue(new long[]{30000,10000}).setTaskScheduler(te);
		registry.setApplicationDestinationPrefixes("/app");
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
			@Override
			public WebSocketHandler decorate(WebSocketHandler handler) {
				WebSocketHandlerDecorator decorator = new WebSocketHandlerDecorator(handler){

					@Override
					public void afterConnectionEstablished(WebSocketSession session) throws Exception {
						super.afterConnectionEstablished(session);
						ConnectEvent event = new ConnectEvent(session.getPrincipal());
						event.setType(TYPE.OPEN);
						applicationContext.publishEvent(event);
					}

					@Override
					public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
						super.afterConnectionClosed(session, closeStatus);
						ConnectEvent event = new ConnectEvent(session.getPrincipal());
						event.setType(TYPE.CLOSE);
						applicationContext.publishEvent(event);
					}

					@Override
					public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
						super.handleMessage(session, message);
					}
				};
				return decorator;
			}
		});
	}
	
}
