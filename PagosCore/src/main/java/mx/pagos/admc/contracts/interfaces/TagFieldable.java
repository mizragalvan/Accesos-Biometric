package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.general.exceptions.DatabaseException;

public interface TagFieldable {
    List<TagField> findAll() throws DatabaseException;

    List<TagField> findByTable(String table) throws DatabaseException;
}
