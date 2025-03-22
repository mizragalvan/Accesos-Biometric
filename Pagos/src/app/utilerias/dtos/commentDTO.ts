
// import { CommentType } from 'src/app/shared/enum/commentType.enum' ;
// import { FlowPurchasingEnum } from 'src/app/shared/enum/flowPurchasing.enum' ;

export class CommentDTO {
    idComment: number| undefined ;
    idRequisition: number| undefined ;
    idUser: number| undefined ;
    creationDate: string| undefined ;
    codRetPopup: string| undefined ;
    // flowStatus: FlowPurchasingEnum| undefined ;
    // commentType: CommentType| undefined ;
    commentText: string| undefined ;
    flowStatusString: string| undefined ;
}
