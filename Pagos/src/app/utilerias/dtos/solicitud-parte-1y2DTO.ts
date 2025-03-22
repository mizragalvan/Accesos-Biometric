import { ContratistaEnum } from '../enums/contratistaEnum';
import { CatDocumentType } from './catDocumentTypeDTO';
import { FinancialEntityDTO } from './financialEntity';

export class SolicitudParteUnoyDosDTO {
    idRequisition: number| undefined;
    idApplicant: number| undefined;
    idDocument: number| undefined;
    idFlow: number| undefined;
    updateRequisitionBy: number| undefined;
    idArea: number| undefined;
    idUnit: number| undefined;
    idCompany: number| undefined;
    contractApplicant: ContratistaEnum| undefined;
    contractType: ContratistaEnum| undefined;
    contract: string| undefined;
    fullName: string| undefined;
    documentType: CatDocumentType| undefined;
    dataFinancialEntityList: Array<FinancialEntityDTO>| undefined;
}
