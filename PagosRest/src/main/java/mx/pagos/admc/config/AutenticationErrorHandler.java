package mx.pagos.admc.config;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
 
@ControllerAdvice
public class AutenticationErrorHandler implements AuthenticationEntryPoint {
	

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// 401
		System.out.println("Error 401");
		authException.printStackTrace();
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
		
	}

	  @ExceptionHandler (value = {AccessDeniedException.class})
	  public void commence(HttpServletRequest request, HttpServletResponse response,    AccessDeniedException accessDeniedException) throws IOException {
	    // 403
		  System.out.println("Error 403");
		  accessDeniedException.printStackTrace();
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization Failed : " + accessDeniedException.getMessage());
	  }

	 
	    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
	    public void commence(HttpServletRequest request, HttpServletResponse response,    MissingServletRequestParameterException accessDeniedException) throws IOException {
	  
	    	 System.out.println("Error 400 MissingServletRequestParameterException");
	    	  accessDeniedException.printStackTrace();
	    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
	      
	    }
	    @ExceptionHandler(value = {IllegalArgumentException.class})
	    public void commence(HttpServletRequest request, HttpServletResponse response,    IllegalArgumentException accessDeniedException) throws IOException {
	    	
	    	System.out.println("Error 400 IllegalArgumentException");
//	    	  accessDeniedException.printStackTrace();
	    	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
	    	
	    }
	  
	  @ExceptionHandler (value = {Exception.class})
	  public void commence(HttpServletRequest request, HttpServletResponse response,
	      Exception exception) throws IOException {
	     // 500
		  System.out.println("Error 500");
		  exception.printStackTrace();
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error : " + exception.getMessage());
	  }

	
    
}