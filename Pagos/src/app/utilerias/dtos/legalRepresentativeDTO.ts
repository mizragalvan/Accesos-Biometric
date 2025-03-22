import { PowerDTO } from './powerDTO';

import { FinancialEntityDTO } from './financialEntity';
// import { RecordStatusEnum } from 'src/app/shared/model/security/RecordStatusEnum';

export class LegalRepresentative {
    idLegalRepresentative: number| undefined
    idFinancialEntity: number| undefined
    financialEntityName: String| undefined
    idDga: number| undefined
    name: String| undefined
    powerList: Array<PowerDTO> | undefined;
    signSendDate: String| undefined
    signReturnDate: String| undefined
    isOriginalSignedContDelivered: Boolean | undefined;
    isCopySignedContractDelivered: Boolean | undefined;
    signedContractSendDate: String| undefined
    // status: RecordStatusEnum;
    financialEntitiesList: Array<FinancialEntityDTO> | undefined;
    numberPage: number| undefined
    totalRows: number| undefined
}
