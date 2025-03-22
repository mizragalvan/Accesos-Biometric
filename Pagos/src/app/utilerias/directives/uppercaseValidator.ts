import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function uppercaseValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    // Verificamos si hay al menos una letra mayúscula
    const hasUppercase = /[A-Z]/.test(value);
    return !hasUppercase ? { 'uppercaseRequired': true } : null;
  };
}
