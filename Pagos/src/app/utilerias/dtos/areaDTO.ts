// import { RecordStatusEnum } from 'src/app/shared/model/security/RecordStatusEnum';

export class Area {
    idArea: number | undefined;
    name: String | undefined;
    // status: RecordStatusEnum;
    pageNumber: number | undefined;
    totalRows: number | undefined;
}
export class AreaCat {
    public idArea: number | undefined
    public name: string| undefined
    public status: string| undefined
    public pageNumber: number| undefined
    public totalRows: number| undefined
}
export class EquipoMX {
    public idEquipo: number| undefined
    public name: string| undefined
    public status: boolean| undefined
    public puntos: number| undefined
    public totalGoles: number| undefined
    public score: string| undefined
    public golLocal: number| undefined
    public golVisita: number| undefined


}
