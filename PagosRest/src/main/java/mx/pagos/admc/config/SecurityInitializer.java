package mx.pagos.admc.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import mx.pagos.admc.AppConfig;


public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer { 

	public SecurityInitializer() {
		super(AppConfig.class,SecurityConfig.class,HttpSessionConfig.class,MvcConfig.class
				//,WebSocketStompConfig.class
				); 
	}
}

