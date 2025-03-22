import { Injectable } from '@angular/core';
// import { AuthService } from 'src/app/security/auth.service';
import { AlertController } from '@ionic/angular';


@Injectable({
  providedIn: 'root',
})
export class Globals {
  deviceSessionIdPay: any = null;
  timeLeft: number = 0;
  interval: any;
  step: number | undefined;

  errorStepNum: number[] = [];
  idRequisition: number = 0;
  idDocument: number = 0;
  idPersonality: number = 0;
  documentacionCompleta: boolean | undefined;
  validaSession: Boolean;
  timeLeftBoolean:Boolean =false;

  private inactivityTime: number = 2 * 60 * 1000; // 5 minutos
  private warningTime: number = 1 * 60 * 1000; // 1 minuto antes de la expiración
  private timeoutId: any;
  private warningTimeoutId: any;
  private isWarningShown: boolean = false;
  private isPaused: boolean = false; // Control para pausar temporizadores


  constructor(
    // private authService: AuthService,
    private alertController: AlertController
  ) {
    this.validaSession = false;
    this.startInactivityTimer();
    this.addActivityListeners();
  }



  // validaSesion() {
  //   this.authService.validateSesion().subscribe(res => {
  //     if (res === null || res === undefined || !res) {
  //       // this.notificacionSesionInvalida();
  //       this.authService.logout();
  //       this.pauseTimer();
  //     }
  //   })

  //     // this.notificacionSesionInvalida();
  //     localStorage.removeItem('token');
  //     localStorage.removeItem('menuItems');
  //     localStorage.removeItem('showFlow');
  //     localStorage.removeItem('validFlow');
  //     this.authService.retirnaLogin();

    
  // }

//nuevo
public startInactivityTimer() {
  if (this.isPaused) return; // Si está pausado, no iniciar temporizadores
  this.resetTimers();
  this.timeoutId = setTimeout(() => this.sessionExpired(), this.inactivityTime);
  this.warningTimeoutId = setTimeout(() => this.showWarning(), this.inactivityTime - this.warningTime);
}

public resetTimers() {
  if (this.timeoutId) clearTimeout(this.timeoutId);
  if (this.warningTimeoutId) clearTimeout(this.warningTimeoutId);
  this.isWarningShown = false;
}

public showWarning() {
  if (this.isPaused || this.isWarningShown) return; // No mostrar si está pausado
  this.isWarningShown = true;
  this.alertController
    .create({
      header: 'Warning',
      message: '¿Quieres restaurarla?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          handler: () => {
            console.log('Acción cancelada');
            this.logout();
          },
        },
        {
          text: 'Confirmar',
          role: 'confirm',
          handler: () => {
            console.log('Acción confirmada');
            this.startInactivityTimer(); // Reinicia el temporizador si confirma
          },
        },
      ],
    })
    .then(alert => {
      alert.present();
    })
    .catch(err => {
      console.error('Error presenting alert:', err);
    });
}

public sessionExpired() {
  if (this.isPaused) return; // Evitar la expiración si está pausado
  this.alertController
    .create({
      header: 'Warning',
      message: 'Tu sesión ha caducado. Serás redirigido al inicio de sesión.',
      buttons: ['Aceptar'],
    })
    .then(alert => {
      alert.present();
    })
    .catch(err => {
      console.error('Error presenting alert:', err);
    });
  this.logout();
}

public addActivityListeners() {
  ['click', 'mousemove', 'keydown', 'scroll', 'touchstart'].forEach(event => {
    document.addEventListener(event, this.startInactivityTimer.bind(this));
  });
}

public removeActivityListeners() {
  ['click', 'mousemove', 'keydown', 'scroll', 'touchstart'].forEach(event => {
    document.removeEventListener(event, this.startInactivityTimer.bind(this));
  });
}


public logout() {
  // this.resetTimers();
  this.pauseTimer(); // Detener el temporizador al cerrar sesión
  // this.authService.logout();
}
public pauseTimer() {
  this.isPaused = true; // Marca que los temporizadores están pausados
  this.resetTimers();
  this.removeActivityListeners();
  console.log('Temporizador pausado y eventos de actividad eliminados.');
}

public resumeTimer() {
  this.isPaused = false; // Reanuda los temporizadores
  this.startInactivityTimer();
  this.addActivityListeners();
  console.log('Temporizador reanudado y eventos de actividad añadidos.');
}


}
