package mx.pagos.admc.contracts.interfaces.export;

import mx.pagos.general.exceptions.BusinessException;

public abstract class AbstractExportable {

	public abstract String[][] getCatalogAsMatrix() throws BusinessException;
}
