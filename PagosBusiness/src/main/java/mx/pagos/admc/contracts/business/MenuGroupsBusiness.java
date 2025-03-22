package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.MenuGroupable;
import mx.pagos.admc.contracts.structures.MenuGroup;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

public class MenuGroupsBusiness {
    private MenuGroupable menuGroupable;

    public MenuGroupsBusiness(final MenuGroupable menuGroupableParameter) {
        this.menuGroupable = menuGroupableParameter;
    }

    public final Integer saveOrUpdate(final MenuGroup menuGroup) throws BusinessException {
        try {
            return this.menuGroupable.saveOrUpdate(menuGroup);
        } catch (DatabaseException dataBaseException) {
            throw new BusinessException("Error al guardar datos del grupo", dataBaseException);
        }
    }

    public final RecordStatusEnum changeFlowStatus(final Integer idGroup, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                return this.menuGroupable.changeFlowStatus(idGroup, RecordStatusEnum.INACTIVE);	
            else
                return this.menuGroupable.changeFlowStatus(idGroup, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            throw new BusinessException("Error al cambiar el estatus del grupo", dataBaseException);
        }
    }

    public final List<MenuGroup> findAll() throws BusinessException {
        try {
            return this.menuGroupable.findAll();
        } catch (DatabaseException dataBaseException) {
            throw new BusinessException("Error al obtener datos del grupo", dataBaseException);
        }
    }

    public final List<MenuGroup> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.menuGroupable.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            throw new BusinessException("Error al obtener estatus del grupo", dataBaseException);
        }
    }	
}
