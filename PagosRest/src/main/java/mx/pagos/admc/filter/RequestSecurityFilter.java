package mx.pagos.admc.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.security.structures.UserSession;

import org.apache.log4j.Logger;

public class RequestSecurityFilter implements Filter {
    
	private static final String MESSAGE_USER_NOT_LOGGED_IN_ERROR =
            "La petición no se puede realizar por que el usuario no ha accedido al sistema o se ha terminado la sesión";
    private static final Logger LOG = Logger.getLogger(RequestSecurityFilter.class);
    private static UserSession session;
    private final List<String> noLoginRequiredUrlsList = new ArrayList<>();
    
    public final void setUserSession(final UserSession userSessionParameter) {
        session = userSessionParameter;
    }

    @Override
    public final void init(final FilterConfig filterConfig) throws ServletException {
        this.setNoLoginRequiredUrlsToList();
    }

    @Override
    public final void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain filterChain) throws IOException, ServletException {
        
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String servletPath = httpRequest.getServletPath();
        
        if (this.isNoRequiredLoginUrl(servletPath) || this.isUserLoggedIn()) {
            filterChain.doFilter(request, response);
        } else {
            LOG.error(MESSAGE_USER_NOT_LOGGED_IN_ERROR);
            LOG.error("Servicio solicitado: " + servletPath);
            LOG.error("IP del solicitante: " + httpRequest.getRemoteAddr() + ":" + httpRequest.getRemotePort());
            LOG.error("Host del solicitante: " + httpRequest.getRemoteHost());
            httpResponse.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_NOT_LOGGED);
            httpResponse.setHeader(Constants.HEADER_MESSAGE, MESSAGE_USER_NOT_LOGGED_IN_ERROR);
        }
    }

    private boolean isNoRequiredLoginUrl(final String servletPath) {
        for (String url : this.noLoginRequiredUrlsList) {
            if (url.equals(servletPath.substring(1)))
                return true;
        }
        return false;
    }
    
    private void setNoLoginRequiredUrlsToList() {
        this.noLoginRequiredUrlsList.add(UrlConstants.FIND_LOGIN_TYPE);
        this.noLoginRequiredUrlsList.add(UrlConstants.IS_INITIAL_SYSTEM_CONFIGURATION);
        this.noLoginRequiredUrlsList.add(UrlConstants.VALIDATE_SESSION);
        this.noLoginRequiredUrlsList.add(UrlConstants.LOGIN);
        this.noLoginRequiredUrlsList.add(UrlConstants.FEED_SESSION);
        this.noLoginRequiredUrlsList.add(UrlConstants.CLOSE_SESSION);
        this.noLoginRequiredUrlsList.add(UrlConstants.SAVE_INITIAL_SYSTEM_CONFIGURATION);
        this.noLoginRequiredUrlsList.add(UrlConstants.RECOVERY_PASSWORD);
        this.noLoginRequiredUrlsList.add(UrlConstants.FIND_VERSION_SYSTEM);
    }

    private boolean isUserLoggedIn() {
        return session != null && session.getUsuario() != null;
    }

    @Override
    public void destroy() { }
}
