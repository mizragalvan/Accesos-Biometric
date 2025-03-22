package mx.pagos.admc.contracts.interfaces.owners;

import java.util.List;

import mx.pagos.admc.contracts.structures.owners.OrganizationEntity;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface OrganizationEntitiable {
    Integer saveOrUpdate(OrganizationEntity organizationEntity) throws DatabaseException;
    
    void changeStatus(Integer idOrganizationEntity, RecordStatusEnum status) throws DatabaseException;
    
    OrganizationEntity findById(Integer idOrganizationEntity) throws DatabaseException, EmptyResultException;
    
    List<OrganizationEntity> findAll() throws DatabaseException;
    
    List<OrganizationEntity> findByStatus(RecordStatusEnum status) throws DatabaseException;
    
    List<OrganizationEntity> findAllOrganizationEntityCatalogPaged(
            RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countTotalItemsToShowOfOrganizationEntity(RecordStatusEnum status) throws DatabaseException;
}
