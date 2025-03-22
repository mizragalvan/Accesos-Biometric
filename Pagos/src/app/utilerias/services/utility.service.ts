export class UtilityService {

     validarCadena(value: string): string {
        if (value === undefined || value === null) {
            return "";
        }
        if (value.trim() === '') {
            return "";
        }
        return value.trim();
    }

    valido(value: any): boolean{
        return value !== null && value !== undefined;
    }

    listaVacia(lista: any[]): boolean{
        return lista === null || lista === undefined || lista.length === 0;
    }

    validarEntero(pValue: any): number{
        if(this.valido(pValue)){
            try {
                const pResultado = Number(pValue);
                return !Number.isNaN(pResultado) ? pResultado : -1;
            }catch{
                return -1;
            }
        }else {
            return -1;
        }
    }

    validarCodigoExitoso(pValue: any): boolean {
        if(this.valido(pValue)){
           const codResultado = this.validarEntero(pValue.codigo);
           return codResultado == 200;
        }
        return false;
    }
}



