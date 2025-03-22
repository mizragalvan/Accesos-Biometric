package mx.pagos.filter.compress;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GzipFilter implements Filter {

	private static final String GZIP = "gzip";

	@Override
	  public void init(final FilterConfig filterConfig) throws ServletException {
		
	  }

	  @Override
	  public void destroy() {
		  
	  }

	  public final void doFilter(final ServletRequest request, final ServletResponse response, 
			  final FilterChain chain) throws IOException, ServletException {
		  
		  final HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
		  final HttpServletResponse httpResponse = (HttpServletResponse) response;
		  
		  if (httpRequest.getServletPath().endsWith(".do")) {
			  chain.doFilter(httpRequest, httpResponse);
		  } else {
			    if (this.acceptsGZipEncoding(httpRequest)) {
			    	try {
					      httpResponse.addHeader("Content-Encoding", GZIP);
					      final GZipResponseWrapper gzipResponse = 
					    		  new GZipResponseWrapper(httpResponse);
					      chain.doFilter(httpRequest, gzipResponse);
					      gzipResponse.close();
			    	} catch (Exception exception) {
			    		chain.doFilter(httpRequest, httpResponse);
			    	}
			    } else {
			      chain.doFilter(httpRequest, httpResponse);
			    }
		  }
	  }

	  private boolean acceptsGZipEncoding(final HttpServletRequest httpRequest) {
		  try {
			  final String acceptEncoding = httpRequest.getHeader("Accept-Encoding");
		      return acceptEncoding != null && acceptEncoding.indexOf(GZIP) != -1;
		  } catch (Exception exception) {
			  return false;
		  }
	  }
	}