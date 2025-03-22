package mx.pagos.admc.filter;
//package mx.contratos.admc.filter;
//
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.stereotype.Component;
//
//@Component("CsrfSecurityRequestMatcher")
//public class CsrfSecurityRequestMatcher implements RequestMatcher {
//    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS|POST)$");
//
//    @Override
//    public boolean matches(final HttpServletRequest request) {          
//        return !(allowedMethods.matcher(request.getMethod()).matches() &&
//                request.getHeader("referer").contains("AdmContract"));
//    }
//}