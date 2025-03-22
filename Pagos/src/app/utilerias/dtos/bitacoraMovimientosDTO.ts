export class BitacoraMovimiento {
    
        idBinnacle: string;
        idUser: string;
        userFullName: string;
        idFlow: string;
        flowName: string;
        registerDate: string;
        action: string
        logCategory: string;
        logList:any [];
        startDate: string;
        endDate: string;
    
        constructor() {
        this.idBinnacle = "";
        this.idUser= "";
        this.userFullName= "";
        this.idFlow= "";
        this.flowName= "";
        this.registerDate= "";
        this.action= "";
        this.logCategory= "";
        this.logList= [];
        this.startDate= "";
        this.endDate= "";
        }
}