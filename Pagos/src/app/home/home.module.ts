import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomePage } from './home.page';

import { HomePageRoutingModule } from './home-routing.module';
import { FingerprintAIO } from '@ionic-native/fingerprint-aio/ngx';


@NgModule({
  imports: [
    CommonModule,          // Para usar directivas comunes de Angular como *ngIf, *ngFor, etc.
    FormsModule,           // Para el uso de [(ngModel)].
    ReactiveFormsModule,   // Para manejar formularios reactivos (formGroup y formControl).
    IonicModule,           // Para componentes de Ionic como ion-card, ion-button, etc.
    HomePageRoutingModule,  // Configuración de las rutas específicas para la página.

  ],
  declarations: [HomePage],
  providers: [
    FingerprintAIO,  // Agrega FingerprintAIO a los providers
  ]
})
export class HomePageModule {}
