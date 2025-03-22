package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface LegalRepresentativeable {
    Integer saveOrUpdate(final LegalRepresentative legalRepresentative) throws DatabaseException;

    void changeLegalRepresentativeStatus(final Integer idLegalRepresentative, final RecordStatusEnum status)
            throws DatabaseException;

    List<LegalRepresentative> findAll() throws DatabaseException;

    List<LegalRepresentative> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;

    List<LegalRepresentative> findByDgaAndFinantialEntity(final List<Integer> areaList)
            throws DatabaseException;

    Power findPowersByIdLegalRepresentativeAndIdFinancialEntity(final Integer idLegalRepresentative,
            Integer idFinancialEntity) throws DatabaseException;

    LegalRepresentative findByIdLegalRepresentative(final Integer idLegalRepresentative) throws DatabaseException;

    void savePower(final Power power) throws DatabaseException;

    void updatePower(final Power power) throws DatabaseException;
    
    void deleteLegalRepresentativePower(Integer idLegalRepresentative) throws DatabaseException;

    void saveLegalRepresentativeFinancialEntity(Integer idLegalRepresentative,
            Integer idFinancialEntity) throws DatabaseException;

    List<FinancialEntity> findFinantialEntitiesByIdLegalRepresentative(
            Integer idLegalRepresentative) throws DatabaseException;

    void deleteFinantialEntitiesByIdLegalRepresentative(Integer idLegalRepresentative) throws DatabaseException;

    List<FinancialEntity> findRequisitionFinantialEntitiesByIdLegalRepresentative(
            Integer idRequisition, Integer idLegalRepresentative) throws DatabaseException;
    
    List<LegalRepresentative> findAllLegalRepresentativeCatalogPaged(LegalRepresentative legalRepresentative,
            Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countTotalItemsToShowOfLegalRepresentative(LegalRepresentative legalRepresentative) throws DatabaseException;
}
