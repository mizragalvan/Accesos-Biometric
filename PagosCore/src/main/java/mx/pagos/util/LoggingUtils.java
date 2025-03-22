package mx.pagos.util;

import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.logs.structures.Binnacle;
import mx.pagos.security.structures.UserSession;

public final class LoggingUtils {
    private LoggingUtils() { }
    
    public static Binnacle createBinnacleForLogging(final String action, final UserSession userSession,
    		final LogCategoryEnum category) {
        final Binnacle binnacle = new Binnacle();
        binnacle.setIdUser(userSession.getIdUsuarioSession());
        binnacle.setIdFlow(userSession.getIdFlow());
        binnacle.setAction(action);
        binnacle.setLogCategory(category);
        return binnacle;
    }
    
    public static native void console(String text)
    /*-{
    console.log(text);
    }-*/;
}