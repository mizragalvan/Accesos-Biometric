import { ValidityEnum } from '../enums/validityEnum';
import { FinancialEntityDTO } from './financialEntity';
import { LegalRepresentative } from './legalRepresentativeDTO';

export class Instrument {
    validity: ValidityEnum | undefined; // Validity
    automaticRenewal: Boolean| undefined // AutomaticRenewal
    renewalPeriods: number| undefined // RenewalPeriods
    validityStartDate: String| undefined // ValidityStartDate
    validityEndDate: String| undefined // ValidityEndDate
    financialEntityAddress: String| undefined // FinancialEntityAddress
    dataFinancialEntityList: Array<FinancialEntityDTO> | undefined;
    legalRepresentativesList: Array<LegalRepresentative> | undefined;
    serviceDescription: String| undefined // ServiceDescription
    technicalDetails: String| undefined // TechnicalDetails
    idRequisition: number| undefined
}
