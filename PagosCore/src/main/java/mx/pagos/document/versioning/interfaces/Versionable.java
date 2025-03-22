package mx.pagos.document.versioning.interfaces;

import java.io.File;
import java.util.List;

import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface Versionable {
    Integer save(Integer idDocument, File path, Integer idUser) throws DatabaseException;
    
    List<Version> findByIdDocument(Integer idDocument) throws DatabaseException;
    
    Version findDocumentVersion(Integer idDoc, Integer version) throws DatabaseException, EmptyResultException;
    
    Version findByIdVersion(Integer idVersion) throws DatabaseException, EmptyResultException;

    void deleteByIdDocument(Integer idDocument) throws DatabaseException;
    
    VersionDTO findDocumentVersionDTO(final Integer idDoc, final Integer versionNumber) throws DatabaseException, EmptyResultException;
    
    void saveContractVersion(final Integer idRequisition, final Integer idDocument) throws DatabaseException;
    
    List<VersionDTO> findContractVersionDTO(final Integer idRequisition) throws DatabaseException, EmptyResultException;
}
