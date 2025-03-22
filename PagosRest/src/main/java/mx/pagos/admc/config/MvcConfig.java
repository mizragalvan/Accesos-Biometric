package mx.pagos.admc.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mx.pagos.admc.util.shared.Constants;

@Configuration
public class MvcConfig implements WebMvcConfigurer {


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
		.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("*")
		.allowedHeaders(
				Constants.HEADER_CONTET,
				Constants.HEADER_RESQUESTWITH_KEY,
				Constants.HEADER_TOKEN_AUT , 
				Constants.HEADER_AUTHORIZACION_KEY)
		.exposedHeaders(
				Constants.HEADER_MESSAGE, 
				Constants.HEADER_STATUS,
				Constants.HEADER_TOKEN_AUT) 
		.allowCredentials(false).maxAge(3600);
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
				.resourceChain(false);
	}

	@Bean
	@Order(0)
	public MultipartFilter multipartFilter() {
	    return new MultipartFilter();
	}
	    
	    @Bean
	    public CommonsMultipartResolver multipartResolver() {
	        return new CommonsMultipartResolver();
	    }

	    @Bean
	    
	    public MultipartConfigElement multipartConfigElement() {
	        MultipartConfigFactory factory = new MultipartConfigFactory();
	        DataSize fsize=DataSize.ofMegabytes(500);
	        factory.setMaxFileSize(fsize);
	        factory.setMaxRequestSize(fsize);
	        
	        return factory.createMultipartConfig();
	    }
}
