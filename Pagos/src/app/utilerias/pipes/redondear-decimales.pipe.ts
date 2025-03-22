import { Pipe, PipeTransform } from '@angular/core';
import { CurrencyPipe } from '@angular/common';

@Pipe({ name: 'darFormatoMoneda' })
export class RedondearDecimalesPipe implements PipeTransform {

  currency: string;
  symbol: boolean;

  constructor(private currencyPipe: CurrencyPipe) {
    this.currency = 'MXN';
    this.symbol = true;
  }

  transform(value: any): any {
    if (value != null && value !== '') {
      value = value.replace(/[$]/, '');
      value = value.replace(/,/g, '');
      if (value != null && value !== '') {
        return this.currencyPipe.transform(value, this.currency, this.symbol);
      }
    }
    return "";
  }


  transformNumber(value: any): string {
    if (value != null && value !== '') {
      const after = this.transform(value);
      return after;
    } else {
      return "";
    }
  }

}
