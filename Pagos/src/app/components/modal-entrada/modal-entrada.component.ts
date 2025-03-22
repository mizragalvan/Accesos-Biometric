import { Component, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';

import { addIcons } from 'ionicons';
import { personCircle } from 'ionicons/icons';

@Component({
  selector: 'app-modal-entrada',
  standalone:false,
  templateUrl: './modal-entrada.component.html',
  styleUrls: ['./modal-entrada.component.scss'],
})
export class ModalEntradaComponent  implements OnInit {

  horaEntrada: string | null = null; // Almacena la hora seleccionada


  // constructor() {
  //   /**
  //    * Any icons you want to use in your application
  //    * can be registered in app.component.ts and then
  //    * referenced by name anywhere in your application.
  //    */
  //   addIcons({ personCircle });
  // }
  constructor(private modalController: ModalController) {}

  // onEntradaChange(event: any) {
  //   this.horaEntrada = event.detail.value; // Captura el valor seleccionado
  // }

  // dismiss() {
  //   // Envía la hora seleccionada al cerrar el modal
  //   this.modalController.dismiss({
  //     horaEntrada: this.horaEntrada,
  //   });
  // }
  onEntradaChange(event: any) {
    this.horaEntrada = event.detail.value; // Captura el valor seleccionado
  }

  // Método para cerrar el modal y pasar la hora seleccionada
  dismissModal() {
    this.modalController.dismiss({
      horaEntrada: this.horaEntrada, // Retorna la hora seleccionada al componente principal
    });
  }

  ngOnInit() {}

}
