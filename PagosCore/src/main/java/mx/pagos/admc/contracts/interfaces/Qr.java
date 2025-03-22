package mx.pagos.admc.contracts.interfaces;

import mx.pagos.admc.contracts.structures.QuickResponse;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface Qr {

	Integer saveQr(final QuickResponse quickResponse, String qr) ;

	QuickResponse findBySalt(final String salt) throws DatabaseException, EmptyResultException ;

}
