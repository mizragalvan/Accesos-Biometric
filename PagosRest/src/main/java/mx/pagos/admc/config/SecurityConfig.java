package mx.pagos.admc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
	
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable()
        .authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		.antMatchers("/*").permitAll()
		.antMatchers("/swagger-ui/**").permitAll()
		.antMatchers("/csws**").permitAll()
		.antMatchers("/assets/**").permitAll()
		.antMatchers("/v2/**").permitAll()
		.antMatchers("/controller/*.go").permitAll()
		.antMatchers("/controller/*.do").permitAll().and()
        .exceptionHandling()
        .authenticationEntryPoint(new AutenticationErrorHandler())
		;
		
	}


	
}