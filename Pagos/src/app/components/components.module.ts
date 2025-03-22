import { NgModule } from "@angular/core";
import { ModalEntradaComponent } from "./modal-entrada/modal-entrada.component";
import { ModalSalidaComponent } from "./modal-salida/modal-salida.component";
import { CommonModule } from "@angular/common";
import { IonicModule } from "@ionic/angular";

@NgModule({
    declarations: [
      ModalEntradaComponent,
      ModalSalidaComponent,
    ],
    imports: [
      CommonModule,
      IonicModule,
    ],
    exports: [
      ModalEntradaComponent,
      ModalSalidaComponent,
    ],
  })
  export class ComponentsModule {}
  