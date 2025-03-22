import { ContratistaEnum } from '../enums/contratistaEnum';

export class ClauseDTO {
    depositAmount: String| undefined ;
    rentEquivalent: string| undefined ;
    surface: String| undefined ;
    propertyAddress: String| undefined ;
    monthlyRentAmount: String| undefined ;
    monthlyRentCurrency: String| undefined ;
    propertyBusinessLine: String| undefined ;
    idRequisition: number | undefined;
    contractObject: String| undefined ;
    considerationClause: String| undefined ;
    clausulaFormaPago: String| undefined ;
    contractObjectClause: String| undefined ;
    validityStartDate: String| undefined ;
    validityEndDate: String| undefined ;
}
