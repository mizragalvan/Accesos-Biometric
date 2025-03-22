import { StatusProductoEnum } from "../enums/statusProductoEnum";
export class Productos {
    idProducto: string;
    codigoBarra: string;
    nombre: string;
    categoria: string;
    precio: string;
    cantidadDisponible: number;
    proveedor:string;
    imagen:string;
    cantidadVendida:number;
    descripcion:string;
    estatus:StatusProductoEnum;
 
    constructor() {
     this.idProducto = ""; 
     this.codigoBarra = "";
     this.nombre = "";
     this.categoria = "";
     this.precio = "";
     this.cantidadDisponible=0;
     this.proveedor="";
     this.imagen="";
     this.cantidadVendida=0;
     this.descripcion="";
     this.estatus=StatusProductoEnum.ACTIVO;


    }
 }