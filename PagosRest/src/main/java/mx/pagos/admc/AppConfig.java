package mx.pagos.admc;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@ComponentScan({ "mx.pagos.*" })
public class AppConfig extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AppConfig.class, args);
	}

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
		return new Angular2PathLocationStrategyCustomizer();
	}

	private static class Angular2PathLocationStrategyCustomizer
			implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
		@Override
		public void customize(ConfigurableServletWebServerFactory factory) {
			factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/"));
		}
	}

	@Bean
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public mx.pagos.security.structures.UserSession UserSession() {
		return new mx.pagos.security.structures.UserSession();
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		AnnotationConfigWebApplicationContext contexto = new AnnotationConfigWebApplicationContext();
//		contexto.setServletContext(servletContext);
//		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(contexto));
//		servlet.setLoadOnStartup(1);
//		servlet.setAsyncSupported(true);
//		servlet.addMapping("/");
//	}

//	@Bean
//	public FilterRegistrationBean<RequestSecurityFilter> loggingFilter() {
//		FilterRegistrationBean<RequestSecurityFilter> registrationBean = new FilterRegistrationBean<>();
//		registrationBean.setFilter(new RequestSecurityFilter());
//		registrationBean.addUrlPatterns("*.do");
//		return registrationBean;    
//	}

}
