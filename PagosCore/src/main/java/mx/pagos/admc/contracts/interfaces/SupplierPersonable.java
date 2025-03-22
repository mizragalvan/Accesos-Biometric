package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface SupplierPersonable {

	Integer save(SupplierPerson supplierPerson) throws DatabaseException;

	List<SupplierPerson> findSupplierPersonsByIdSupplierAndType(Integer idSupplier,
			SupplierPersonTypeEnum supplierPersontype) throws DatabaseException;

	void deleteSupplierPersonByIdSupplierAndType(Integer idSupplier, SupplierPersonTypeEnum supplierPersontype)
			throws DatabaseException;

	void saveLegalRepresentativePower(Power power) throws DatabaseException;

	void deleteLegalRepresentativesPowers(Integer idSupplier) throws DatabaseException;

	String findLegalRepresentativePower(Integer idSupplierPerson) throws DatabaseException;

	List<SupplierPerson> findSupplierLegalRepresentativesPower(Integer idSupplier) throws DatabaseException;

	void updateSupplier(final SupplierPerson supplierPerson) throws DatabaseException;

	List<SupplierPerson> findSupplierPersonByIdSupplierAndTypeAndName(final Integer idSupplier,
			final SupplierPersonTypeEnum supplierPersontype, final String name) throws DatabaseException;
	
	List<SupplierPerson> findLegalRepresentativesByIdRequisition(final Integer idRequisition, final SupplierPersonTypeEnum supplierPersontype) throws DatabaseException;

	void changePersonSupplierStatus(SupplierPerson supplierPerson) throws DatabaseException;

	List<SupplierPerson> findSupplierPersonsByIdSupplierAndTypeAtive(
            final Integer idSupplier, final SupplierPersonTypeEnum type, final boolean active) throws DatabaseException;
}
