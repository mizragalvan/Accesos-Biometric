import { Component, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';

@Component({
  selector: 'app-modal-salida',
  standalone:false,
  templateUrl: './modal-salida.component.html',
  styleUrls: ['./modal-salida.component.scss'],
})
export class ModalSalidaComponent  implements OnInit {
  horaSalida: string | null = null; // Almacena la hora seleccionada

  constructor(private modalController: ModalController) {}

  // Método para manejar el cambio de hora
  onSalidaChange(event: any) {
    this.horaSalida = event.detail.value; // Captura el valor seleccionado
  }

  // Método para cerrar el modal y pasar la hora seleccionada
  dismissModal() {
    this.modalController.dismiss({
      horaSalida: this.horaSalida, // Retorna la hora seleccionada al componente principal
    });
  }
  ngOnInit() {}

}
