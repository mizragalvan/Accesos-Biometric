package mx.pagos.admc.contracts.interfaces.owners;

import java.util.List;

import mx.pagos.admc.contracts.structures.owners.PublicFigure;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.owners.PublicFigureTypeEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface PublicFigurable {

    Integer saveOrUpdate(PublicFigure publicFigure) throws DatabaseException;

    void changeStatus(Integer idPublicFigure, RecordStatusEnum status) throws DatabaseException;

    PublicFigure findById(Integer idPublicFigure) throws DatabaseException, EmptyResultException;

    List<PublicFigure> findAll() throws DatabaseException;

    List<PublicFigure> findByStatus(RecordStatusEnum status) throws DatabaseException;

    List<PublicFigure> findByType(PublicFigureTypeEnum type) throws DatabaseException;

    List<PublicFigure> findAllPublicFigureCatalogPaged(RecordStatusEnum status, PublicFigureTypeEnum type,
            Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countTotalItemsToShowOfPublicFigure(RecordStatusEnum status, PublicFigureTypeEnum type)
            throws DatabaseException;
}
