import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

export class Constants {
    static readonly DATE_FMT = 'dd/MM/yyyy';
    static readonly DATE_TIME_FMT = `${Constants.DATE_FMT} hh:mm:ss`;
}

@Pipe({ name: 'darFormatoFecha' })
export class FormatofechaPipe extends DatePipe implements PipeTransform {

    override transform(value: any): any {
        if (value !== undefined && value !== null && value !== '') {
            return super.transform(value, Constants.DATE_FMT);
        }
    }
}

