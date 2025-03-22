package mx.pagos.admc.filter;
//package mx.contratos.admc.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component("FilterSeguridad")
//public class FilterSeguridad implements Filter {
//  
//	private static final Logger LOG = Logger.getLogger(FilterSeguridad.class);
//	
//	@Override
//	public void destroy() {
//
//	}
//
//	@Override
//	public final void doFilter(final ServletRequest request, final ServletResponse response,
//	        final FilterChain filterChain) throws IOException, ServletException {
//		
//		final HttpServletRequest httpRequest = (HttpServletRequest) request;
//		@SuppressWarnings("unused")
//		final HttpServletResponse httpResponse = (HttpServletResponse) response;
//		final String servletPath = httpRequest.getServletPath();
//		
//	    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    LOG.info("En Active Directory");
//		if (authentication != null && authentication.isAuthenticated()) {
//		    LOG.info("Usuario Valido en Active Directory");
//		    LOG.info("servletPath :: " + servletPath);
//		    LOG.info("Name :: " + authentication.getName());
//		    
//		    // Some Code
//
//			filterChain.doFilter(request, response);
//		}
//	}
//	
//	@Override
//	public void init(final FilterConfig arg0) throws ServletException {
//		
//	}
//}
