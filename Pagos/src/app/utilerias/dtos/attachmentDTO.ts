// import { InfoArchivoUpload } from 'src/app/solicitud/dto/info-archivo-uploadDTO';

export class AttachmentDTO {
    idRequisition: number | undefined| undefined
    businessReasonMonitoringPlan: String| undefined // BusinessReasonMonitoringPlan
    attatchmentOthersName: String| undefined // AttatchmentOthersName
    attatchmentDeliverables: Boolean| undefined // AttatchmentDeliverables
    attchmtServiceLevelsMeasuring: Boolean| undefined // AttchmtServiceLevelsMeasuring
    attatchmentPenalty: Boolean| undefined // AttatchmentPenalty
    attatchmentScalingMatrix: Boolean| undefined // AttatchmentScalingMatrix
    attatchmentCompensation: Boolean| undefined // AttatchmentCompensation
    attchmtBusinessMonitoringPlan: Boolean| undefined // AttchmtBusinessMonitoringPlan
    attchmtImssInfoDeliveryReqrmts: Boolean| undefined // AttchmtImssInfoDeliveryReqrmts
    attatchmentInformationSecurity: Boolean| undefined // AttatchmentInformationSecurity
    attatchmentOthers: Boolean| undefined // AttatchmentOthers
    // documentsAttachment: Array<InfoArchivoUpload>| undefined
}
