export class TipoPersonalidad {
   idPersonality: string;
   idRequiredDocumentList: string;
   name: string;
   personalityEnum: string;
   status: string;

   constructor() {
    this.idPersonality = ""; 
    this.idRequiredDocumentList = "";
    this.name = "";
    this.personalityEnum = "";
    this.status = "";
   }
}