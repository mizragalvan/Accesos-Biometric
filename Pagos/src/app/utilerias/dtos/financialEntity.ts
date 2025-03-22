
import { PowerDTO } from './powerDTO';
// import { RecordStatusEnum } from 'src/app/shared/model/security/RecordStatusEnum';

export class FinancialEntityDTO {
    idFinancialEntity: number| undefined
    name: String| undefined
    financialEntityName: String| undefined
    // status: RecordStatusEnum;
    longName: String| undefined
    domicile: String| undefined
    idArticleOfLaw: number| undefined
    idConfidentialityLaw: number| undefined
    powerList: Array<PowerDTO> | undefined;
    rfc: String| undefined
    telefono: String| undefined
    correo: String| undefined
    atencion: String| undefined
    constitutive: String| undefined
    constitutiveEnglish: String| undefined
    treatment: String| undefined
    accountNumber: String| undefined
    bankBranch: String| undefined
    bankingInstitution: String| undefined
    numberPage: number| undefined
    totalRows: number| undefined
    power: PowerDTO | undefined;
    idLegalRepresentative: number| undefined
    legalRepresentativeName: String| undefined
    idRequisition: number| undefined
    phone: String| undefined
    email: String| undefined
    attention: String| undefined
}
