import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {DocumentDS} from '../../../../shared/model/digital-signature/DocumentDS.model';
import {Recipient} from '../../../../shared/model/digital-signature/Recipient.model';
import {MensajesFijosEnum} from '../../../../shared/enum/mensajes-fijos-enum';
import {ConstantesURL} from '../../../../utilerias/dtos/urlConstantes';
import {BandejaService} from '../../../services/bandejas.service';
import Swal from 'sweetalert2';
import {Sweetalert} from '../../../../shared/SweetAlert';
import {RecipientInformationModalComponent} from '../recipient-information-modal/recipient-information-modal.component';
import {MatDialog} from '@angular/material/dialog';
import {DocumentInformationModalComponent} from '../document-information-modal/document-information-modal.component';
import {DigitalSignatureStatusEnum} from '../../../../utilerias/enums/DigitalSignatureStatusEnum';
import {Constantes} from '../../../../shared/constantes/constantes';
import {renderFlagCheckIfStmt} from "@angular/compiler/src/render3/view/template";
import {RecipientActionEnum} from "../../../../shared/enum/recipient-action-enum";
import { SweetAlertComponent } from 'src/app/shared/SweetMesssage';
import { FormGroup } from '@angular/forms';

@Component({
    selector: 'app-document-status-dashboard',
    templateUrl: './document-status-dashboard.component.html',
    styleUrls: ['./document-status-dashboard.component.css']
})
export class DocumentStatusDashboardComponent implements OnInit, AfterViewInit {

    @Input() folio: string;

    documentDS: DocumentDS;
    isDigitalSignature: boolean = false;
    ZIP_FILE_EXTENSION: string = '.zip';

    digitalSignatureStatus : typeof DigitalSignatureStatusEnum = DigitalSignatureStatusEnum;
    recipientActionEnum : typeof RecipientActionEnum = RecipientActionEnum;
    
    hasDocumentByIdRequisition: boolean = false;
    showSignatureSelection: boolean = false;
    showDigitalSignature: boolean = false;
    showPrintedSignature: boolean = false;
    plantilla: boolean;
    documentDs: DocumentDS = new DocumentDS();
    prepareForm: FormGroup;
    statusDigitalSignature:string;
    estadoSobre:any;
    constructor(private service: BandejaService, private dialog: MatDialog,private mensaje: SweetAlertComponent,) {
    }

    ngOnInit() {
        this.getInfoDocument();
    }

    ngAfterViewInit(): void {
        this.getDsDocument();
    }

    getDsDocument() {
        let documentDS = new DocumentDS();
        documentDS.idRequisition = Number(this.folio);
        this.service.post(ConstantesURL.DS_GET_DOCUMENT_BY_ID_REQUISITION, documentDS).toPromise().then(response => {
            console.log('response', response);
            if (response.responseCode === Constantes.ERROR_DS_DOCUMENT_NOT_FOUND) {
                this.isDigitalSignature = false;
                return;
            }

            if (response.responseCode === Constantes.ERROR_DS_DOCUMENT_WITHOUT_RECIPIENTS) {
                Sweetalert.show('error', null, MensajesFijosEnum.ERROR_DS_DOCUMENT_WITHOUT_RECIPIENTS, () => {
                    // this.router.navigate(['inicio']); // TODO
                });
                return;
            }

            this.documentDS = response;
            this.isDigitalSignature = true;
            setTimeout(() => { this.showStatus(); }, 1000);

        }).catch(e => {
            console.log(e);
            Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, () => {
                // this.router.navigate(['inicio']); // TODO
            });
        });
    }

    showStatus() {
        const progressEl = document.getElementById("progress");
        const circles = document.querySelectorAll(".circle");

        let currentStatus: number = this._getCurrentStatus();

        circles.forEach((circle, indx) => {
            if (indx < currentStatus) {
                circle.classList.add("active");
            } else {
                circle.classList.remove("active");
            }
        });

        const circlesActives = document.querySelectorAll(".active");
        const progressPercentage = ((circlesActives.length - 2) / (circles.length - 1)) * 100 + "%";

        setTimeout(() => {
            progressEl.style.width = progressPercentage;
        }, 500);
    }

    private _getCurrentStatus(): number {
        switch (this.documentDS.statusDigitalSignature) {
            case DigitalSignatureStatusEnum.SENT:
                return 1;
            case DigitalSignatureStatusEnum.IN_PROGRESS:
                return 2;
            case DigitalSignatureStatusEnum.SIGNED:
                return 3;
            default:
                return 0;
        }
    }

    showRecipientInformation(recipient: Recipient, document: DocumentDS) {
        this.dialog.open(RecipientInformationModalComponent, {
            disableClose: true,
            autoFocus: true,
            panelClass: 'custom-modalbox',
            data: {recipient: recipient, document: document}
        });
    }

    signDocument(recipient: Recipient) {
        window.open(recipient.linkToDocument, '_blank');
    }

    copyInviteUrl(recipient: Recipient) {
        const textArea = document.createElement('textarea');
        textArea.value = recipient.linkToDocument;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);

        const message: string = MensajesFijosEnum.DS_COPY_INVITE_URL;
        Sweetalert.show('success', null, message, null);
    }

    resendInvitationEmail(document: DocumentDS, recipient: Recipient) {
        const documentCopy: DocumentDS = JSON.parse(JSON.stringify(document));
        documentCopy.recipients = [];
        documentCopy.recipients.push(recipient);
        // this.service.post(ConstantesURL.DS_REENVIAR_SIGNED_DOCUMENT, documentCopy).toPromise().then(response => {
        // }).catch( e => {
        //     console.log(e);
        //     this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
        // });
        this.service.post(ConstantesURL.DS_RESEND_INVITATION_EMAIL, this.documentDS).toPromise().then(() => {
            Sweetalert.show('success', null, MensajesFijosEnum.DS_RESEND_INVITITATION_EMAIL, null);
        }).catch(e => {
            console.log(e);
            Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
        });
    }
    correctInvitationEmail(document: DocumentDS, recipient: Recipient) {
        const documentCopy: DocumentDS = JSON.parse(JSON.stringify(document));
        documentCopy.recipients = [];
        documentCopy.recipients.push(recipient);
        
        this.service.post(ConstantesURL.DS_CORRECT_SIGNED_DOCUMENT, this.documentDS).toPromise().then(response => {
            Sweetalert.show('success', null, MensajesFijosEnum.DS_RESEND_INVITITATION_EMAIL, null);
            this.abrirVentanaEmergente(response.extension);
        }).catch(e => {
            console.log(e);
            Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
        });
    }
    abrirVentanaEmergente(url:string) {
        console.log('response1 es :',url);
        // const ventana = window.open('https://www.tu-ruta.com', '_blank');
        const ventana = window.open(url);
    
        const comprobarCierre = setInterval(() => {
          if (ventana.closed) {
            clearInterval(comprobarCierre);
            this.ventanaCerrada();
          }
        }, 1000);
      }
    
      ventanaCerrada() {
        // alert('El embrujo finalizo xd');
   
      }

    showDocumentInformation() {
        let dialogRef = this.dialog.open(DocumentInformationModalComponent, {
            disableClose: true,
            autoFocus: true,
            panelClass: 'custom-modalbox',
            data: this.documentDS
        });
        dialogRef.afterClosed().subscribe(() => {
            // TODO onClosed
        });
    }

    downloadSignedZip() {
        this.service.postResponseFile(ConstantesURL.DS_DOWNLOAD_SIGNED_DOCUMENT, this.documentDS).toPromise().then(response => {
            Sweetalert.show('success', null, MensajesFijosEnum.DS_DOWNLOAD_DOCUMENT, null);
            const downloadUrl = URL.createObjectURL(response);
            const link = document.createElement('a');
            link.href = downloadUrl;
            const lastDotIndex = this.documentDS.documentName.lastIndexOf('.');
            const nameWithoutExtension = lastDotIndex !== -1
                ? this.documentDS.documentName.substring(0, lastDotIndex)
                : this.documentDS.documentName;
            link.download = nameWithoutExtension + this.ZIP_FILE_EXTENSION;
            link.click();
            URL.revokeObjectURL(downloadUrl);
        }).catch(e => {
            console.log(e);
            Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
        });
    }

    deleteDocument() {
        Swal({
            title: '¿Desea eliminar este documento?',
            html: 'Esta acción es irreversible. Si decide eliminar este documento ' +
                '(<strong>' + this.documentDS.documentName + '</strong>), ' +
                'se borrará automáticamente del sistema y no lo podrá recuperar.',
            input: 'text',
            inputPlaceholder: 'Primera letra del nombre del documento',
            inputAttributes: {
                maxlength: '1'
            },
            showCancelButton: true,
            confirmButtonColor: '#BF4246',
            confirmButtonText: 'Eliminar documento',
            cancelButtonText: 'Cancelar',
            inputValidator: (value) => {
                if (!this.documentDS.documentName.toUpperCase().startsWith(value.toUpperCase())) {
                    return 'Ingrese la primera letra del nombre del documento';
                }
            }
        }).then((result) => {
            if (result.value) {
                this._deleteDocumentService();
            }
        });
    }

    private _deleteDocumentService() {
        this.service.post(ConstantesURL.DS_DELETE_DOCUMENT_BY_ID, this.documentDS).toPromise().then(response => {
            if (response.responseCode !== Constantes.SUCCESS_CODE) {
                Sweetalert.show('error', null, MensajesFijosEnum.ERROR_DS_DOCUMENT_WITHOUT_RECIPIENTS, null);
                return;
            }
            Sweetalert.show('success', null, MensajesFijosEnum.DS_DELETE_DOCUMENT, null);
        }).catch(e => {
            console.log(e);
            Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
        });
    }

    deleteSignedDocument(){
        this.service.post(ConstantesURL.DS_DELETE_SIGNED_DOCUMENT, this._mapFormToPositionDocumentDS()).toPromise().then(response => {
    
          // this.abrirVentanaEmergente(response);
          console.log('response1 es :',response);
    
      }).catch( e => {
          console.log(e);
          // Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
          this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
    
      });
       }
       private _mapFormToPositionDocumentDS(): DocumentDS {
        let documentDS: DocumentDS = new DocumentDS();
        documentDS.idRequisition = parseInt(this.folio);
      
        return documentDS;
      }
      reenvioSignedDocument(){
        this.service.post(ConstantesURL.DS_REENVIAR_SIGNED_DOCUMENT, this._mapFormToPositionDocumentDS()).toPromise().then(response => {
    
          // this.abrirVentanaEmergente(response);
          console.log('response1 es :',response);
    
      }).catch( e => {
          console.log(e);
          // Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
          this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
    
      });
       }
       getDocumentByIdRequisition(){
        let documentDsRequest = new DocumentDS();
        documentDsRequest.idRequisition = parseInt(this.folio);
        this.service.post(ConstantesURL.DS_GET_DOCUMENT_BY_ID_REQUISITION, documentDsRequest).toPromise().then( response => {
    
          if (response !== null && response.responseCode === Constantes.ERROR_CODE) {
            this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
            this.showDigitalSignature = false;
          }
    
          this.hasDocumentByIdRequisition = response !== null;
    
          if (this.hasDocumentByIdRequisition) {
            this.chooseDigitalSignature();
            this.documentDs = response;
            return;
          }
    
          if (!this.hasDocumentByIdRequisition) {
            this.documentDs = null;
            return;
          }
    
        }).catch( e => {
          console.log(e);
          this.showDigitalSignature = false;
          this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
          
        });
      }
      getInfoDocument(){
        // alert('entro a la instancia')
        this.service.post(ConstantesURL.DS_STATUS_DOCUMENT, this._mapFormStatusDocumentDS()).toPromise().then(response => {
          // alert('status :'+response.statusDigitalSignature)
          this.statusDigitalSignature=response.statusDigitalSignature.toString();
          // alert('response : '+response.statusDigitalSignature);
          // alert('statusDigitalSignature : '+this.statusDigitalSignature);
          if(response.statusDigitalSignature.toString()==="sent"){
            // alert('sent :')
            this.estadoSobre= DigitalSignatureStatusEnum.IN_PROGRESS
            this.getStatusDigitalDocument();
            this.getDocumentByIdRequisition();
            this.getDsDocument();

          }
          if(response.statusDigitalSignature.toString()=='completed'){
            // alert('completed :')
            this.estadoSobre= DigitalSignatureStatusEnum.SIGNED
            this.getStatusDigitalDocument();
            this.getStatusRecipientSignedDocument();
            this.getDocumentByIdRequisition();
            this.getDsDocument();

          }
  
      }).catch( e => {
          console.log(e);
          // Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
          // this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
  
      });
      }
      private _mapFormStatusDocumentDS(): DocumentDS {
        let documentDS: DocumentDS = new DocumentDS();
        documentDS.idRequisition = parseInt(this.folio);
        documentDS.onlySigner = true;
        documentDS.recipients = this._getRecipients();
        documentDS.emailMessage = "firma documento";
        documentDS.emailSubject = "gemelo_mizraim@hotmail.com";
    
        return documentDS;
    }
  
      getStatusDigitalDocument(){
        // alert('entro a la instancia')
        this.service.post(ConstantesURL.DS_STATUS_DIGITAL_SIGNATURE_DOCUMENT, this._mapFormStatusToDocumentDS()).toPromise().then(response => {
  
      }).catch( e => {
          console.log(e);
          // Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
          // this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
  
      });
      }
      private _mapFormStatusToDocumentDS(): DocumentDS {
        let documentDS: DocumentDS = new DocumentDS();
        documentDS.idRequisition = parseInt(this.folio);
        documentDS.statusDigitalSignature=this.estadoSobre;
        
        // documentDS.onlySigner = true;
        // documentDS.recipients = this._getRecipients();
        // documentDS.emailMessage = "firma documento";
        // documentDS.emailSubject = "gemelo_mizraim@hotmail.com";
    
        return documentDS;
    }
      getStatusRecipientSignedDocument(){
        // alert('entro a la instancia')
        this.service.post(ConstantesURL.DS_STATUS_RECIPIENT_SIGNED_DOCUMENT, this._mapFormStatusRecipientToDocumentDS()).toPromise().then(response => {
          // alert('entro al service')
          if (response.responseCode !== Constantes.SUCCESS_CODE) {
              Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
              return;
          }
          this.mensaje.showSwal('success-message', MensajesFijosEnum.DS_STATUS_DOCUMENT);
  
      }).catch( e => {
          console.log(e);
          // Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
          this.mensaje.showSwal('error-message', MensajesFijosEnum.DS_ERROR);
  
      });
      }
      private _mapFormStatusRecipientToDocumentDS(): DocumentDS {
        let documentDS: DocumentDS = new DocumentDS();
        documentDS.idRequisition = parseInt(this.folio);
        return documentDS;
    }
    private _getRecipients(): Recipient[] {
        let recipients = [];
        // this.recipients.controls.forEach( recipientForm => {
        //     let recipient: Recipient = new Recipient();
        //     recipient.signingOrder = recipientForm.get('signingOrder').value;
        //     recipient.isRequired = recipientForm.get('isRequired').value;
        //     recipient.email = recipientForm.get('email').value;
        //     recipient.fullName = recipientForm.get('fullName').value;
        //     // recipient.rfc = recipientForm.get('rfc').value;
        //     recipient.recipientAction = recipientForm.get('recipientAction').value;
        //     recipient.secretCode = recipientForm.get('secretCode').value;
        //     recipient.note = recipientForm.get('note').value;
        //     recipients.push(recipient);
        // });
        return recipients;
    }
    chooseDigitalSignature() {
        this.showDigitalSignature = true;
        this.showSignatureSelection = false;
        this.showPrintedSignature = false;
      }

      viewDocumentCompleted(document: DocumentDS, recipient: Recipient) {
        const documentCopy: DocumentDS = JSON.parse(JSON.stringify(document));
        documentCopy.recipients = [];
        documentCopy.recipients.push(recipient);
        
        this.service.post(ConstantesURL.DS_VIEW_SIGNED_DOCUMENT, this.documentDS).toPromise().then(response => {
            Sweetalert.show('success', null, MensajesFijosEnum.DS_RESEND_INVITITATION_EMAIL, null);
            this.abrirVentanaDocumento(response.extension);
        }).catch(e => {
            console.log(e);
            Sweetalert.show('error', null, MensajesFijosEnum.DS_ERROR, null);
        });
    }
    abrirVentanaDocumento(url:string) {
        console.log('response1 es :',url);
        // const ventana = window.open('https://www.tu-ruta.com', '_blank');
        const ventana = window.open(url);
    
        const comprobarCierre = setInterval(() => {
          if (ventana.closed) {
            clearInterval(comprobarCierre);
            this.CerrarVentana();
          }
        }, 1000);
      }
    
      CerrarVentana() {
        // alert('El embrujo finalizo xd');
   
      }