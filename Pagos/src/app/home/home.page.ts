import { Component, OnDestroy, OnInit, ElementRef, ViewChild } from '@angular/core';
import * as faceapi from 'face-api.js';


import { CommonModule } from '@angular/common';
// import { IonicModule } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';
import {
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonButton,
  IonInput,
  IonIcon,
  IonText,
  IonItem,
  IonCard,
  IonRow,
  IonCol,
  IonToast,
} from '@ionic/angular/standalone';
import { BiometryType, NativeBiometric } from 'capacitor-native-biometric';
import { FingerprintAIO } from '@ionic-native/fingerprint-aio/ngx';

import { ModalController } from '@ionic/angular';
import { ModalEntradaComponent } from '../components/modal-entrada/modal-entrada.component';
import { ModalSalidaComponent } from '../components/modal-salida/modal-salida.component';
import { uppercaseValidator } from '../utilerias/directives/uppercaseValidator';
import {
  ExtendedGetResult,
  FingerprintjsProAngularService,
  GetResult,
} from '@fingerprintjs/fingerprintjs-pro-angular';

import { Camera, CameraResultType } from '@capacitor/camera';
import { BarcodeScanner } from '@capacitor-community/barcode-scanner';
import { ConstantesURL } from '../utilerias/dtos/urlConstantes';
// import { reload } from 'ionicons/icons';


@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false,
})
export class HomePage   implements OnInit, OnDestroy {
  form!: FormGroup;
  isPwd = false;
  server = 'www.faceid.technyks.com';
  isToast = false;
  toastMessage!: string;
  mostrarInicio: boolean | undefined;
  mostrarAccesos: boolean | undefined;
  id_empleado:string| undefined;
  horaEntrada: string | undefined;
  horaSalida: string | undefined; 
  resultado: any | undefined;
  error: any | undefined;
  visitorId = 'Press "Identify" button to get visitorId'
  extendedResult: null | ExtendedGetResult | GetResult = null
  employeeName: string = '';
  fingerprints: string[] = [];
  scannedResult: string | null = null;
  
   
  puestos: { value: string; label: string }[] = []; // Arreglo para los datos
  selectedPuesto: string = ''; // Para manejar la selección del usuario

  diasSemana = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
  diasSeleccionados: string[] = [];
  users = [
    { id: 1, name: 'Juan Pérez', isFingerprintRegistered: true, date: new Date() },
    { id: 2, name: 'Ana López', isFingerprintRegistered: false, date: new Date() },
    { id: 3, name: 'Carlos Ramírez', isFingerprintRegistered: true, date: new Date() },
    { id: 4, name: 'María Sánchez', isFingerprintRegistered: true, date: new Date() },
    { id: 5, name: 'Luis Hernández', isFingerprintRegistered: false, date: new Date() },
    { id: 6, name: 'Laura Gómez', isFingerprintRegistered: true, date: new Date() },
  ];
  currentTime: string | undefined;
  private intervalId: any;
  // employee = {
  //   nombre: '',
  //   correo: '',
  //   contraseña: '',
  //   huella: '',
  // };

  @ViewChild('videoElement') videoElement!: ElementRef<HTMLVideoElement>;
  @ViewChild('canvas') canvas!: ElementRef<HTMLCanvasElement>;

  isCameraOn = false;


  constructor(private http:HttpClient,private modalController: ModalController, private faio: FingerprintAIO,private fingerprintService: FingerprintjsProAngularService) {
    this.initForm();
  }

   ngOnInit() {
     // Cargar modelos de reconocimiento facial
      // faceapi.nets.tinyFaceDetector.loadFromUri('/assets/models');
      // faceapi.nets.faceLandmark68Net.loadFromUri('/assets/models');
      // faceapi.nets.faceRecognitionNet.loadFromUri('/assets/models');
      // faceapi.nets.ssdMobilenetv1.loadFromUri('/assets/models');
      // this.startCamera();
     
    this.startClock();
    this.mostrarInicio = true;
    this.mostrarAccesos = false;

    // const body = document.getElementsByTagName('body')[0];
    // body.classList.add('login-page');
    // body.classList.add('off-canvas-sidebar');
    // const card = document.getElementsByClassName('card')[0];
    //  setTimeout(function () {
    //   //  card.classList.remove('card-hidden');
    //  }, 700);
  }

  

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId); // Limpia el intervalo al destruir la página
    }
    const body = document.getElementsByTagName('body')[0];
    // body.classList.remove('login-page');
    // body.classList.remove('off-canvas-sidebar');
  }

  startClock() {
    this.intervalId = setInterval(() => {
      const now = new Date();
      const hours = String(now.getHours()).padStart(2, '0');
      const minutes = String(now.getMinutes()).padStart(2, '0');
      const seconds = String(now.getSeconds()).padStart(2, '0');
      this.currentTime = `${hours}:${minutes}:${seconds}`;
    }, 1000);
  }

  onCodeResult(event: any) {
    console.log('Evento recibido del escáner:', event); // Muestra la estructura en la consola
    if (typeof event === 'string') {
      alert(`Código escaneado: ${event}`);
    } else {
      console.error('Formato inesperado:', event);
    }
  }

  initForm() {
 
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      ape_paterno: new FormControl('', [Validators.required]),
      ape_materno: new FormControl('', [Validators.required]),
      nombre: new FormControl('', [Validators.required]),
      entrada: new FormControl('', [Validators.required]),
      salida: new FormControl('', [Validators.required]),
      puesto: new FormControl('', [Validators.required]),
      // huellaRegistrada: new FormControl(false, [Validators.requiredTrue]), // Agregar la validación requerida
      dias_trabajo: new FormControl([], [Validators.required]),
      id_empleado:new FormControl('',[Validators.required]),

    });

    
     // Datos en duro
     this.puestos = [
      { value: 'developer', label: 'Desarrollador' },
      { value: 'designer', label: 'Diseñador' },
      { value: 'manager', label: 'Gerente' },
    ];

  }

  togglePwd() {
    this.isPwd = !this.isPwd;
  }

  // onSubmit() {
  //   if (this.form.invalid) {
  //     this.form.markAllAsTouched();
  //     return;
  //   }
  //   console.log(this.form.value);
  //   this.saveCredentials(this.form.value);
  // }

  onSubmit() {
    // Valida si el formulario es válido
    if (this.form.valid) {
      // Imprime los valores del formulario
      console.log('Formulario válido. Datos a guardar: ', this.form.value);
    } else {
      console.error('Formulario inválido. Por favor, completa todos los campos requeridos.');
      // Marca todos los controles como "tocados" para mostrar errores
      this.form.markAllAsTouched();
    }
  }

  async performBiometricVerification() {
    try {
      const result = await NativeBiometric.isAvailable({ useFallback: true });
      if (!result.isAvailable) return;

      const isFaceID = result.biometryType == BiometryType.FACE_ID;
      console.log(isFaceID);

      const verified = await NativeBiometric.verifyIdentity({
        reason: 'Authentication',
        title: 'Log in',
        subtitle: 'FACE ID',
        description: 'Ingrese FaceID para Autrorización',
        useFallback: true,
        maxAttempts: 2,
      })
        .then(() => true)
        .catch(() => false);

      if (!verified) return;

      this.getCredentials();
    } catch (e) {
      console.log(e);
    }
  }

  async saveCredentials(data: { email: string; password: string }) {
    try {
      // const result = await NativeBiometric.isAvailable();
      // if (!result.isAvailable) return;
      // Save user's credentials
      await NativeBiometric.setCredentials({
        username: data.email,
        password: data.password,
        server: this.server,
      });

      this.openToast('Login Successful');
    } catch (e) {
      console.log(e);
    }
  }

  async getCredentials() {
    try {
      const credentials = await NativeBiometric.getCredentials({
        server: this.server,
      });
      console.log(credentials);
      this.openToast(`Authorised! Credentials: ${credentials.username}, ${credentials.password}`);
    } catch (e) {
      console.log(e);
    }
  }

  deleteCredentials() {
    // Delete user's credentials
    NativeBiometric.deleteCredentials({
      server: this.server,
    }).then(() => {
      this.openToast('Credentials deleted');
    });
  }

  openToast(msg: string) {
    this.isToast = true;
    this.toastMessage = msg;
  }

  mostrarFrmContrasena() {
    // this.stopCamera();
    this.obtenerUltimoId();

    this.mostrarInicio = !this.mostrarInicio;
    
  }

  async captureFingerprint() {
    // const result = await FingerprintAuth.verify({
    //   reason: 'Captura tu huella digital para el registro',
    // });

    // if (result.verified) {
      // Guardar un identificador ficticio como hash de huella
      // this.employee.huella = 'unique-fingerprint-id-' + new Date().getTime();
      // alert('Huella capturada correctamente');
    // } else {
    //   alert('Error al capturar la huella');
    // }
  }

  registerEmployee() {
    // this.http.post('http://localhost:8080/api/employees', this.employee).subscribe(
    //   () => alert('Empleado registrado con éxito'),
    //   (error) => alert('Error al registrar empleado: ' + error.message)
    // );
  }
  onEntradaChange(event: any) {
    this.horaEntrada = event.detail.value;
    console.log('Hora de entrada:', this.horaEntrada);
  }

  onSalidaChange(event: any) {
    this.horaSalida = event.detail.value;
    console.log('Hora de salida:', this.horaSalida);
  }

  // async openModalEntrada() {
  //   const modal = await this.modalController.create({
  //     component: ModalEntradaComponent,
  //   });
  //   return await modal.present();
  // }

  // async openModalSalida() {
  //   const modal = await this.modalController.create({
  //     component: ModalSalidaComponent,
  //   });
  //   return await modal.present();
  // }
  async openModalEntrada() {
    const modal = await this.modalController.create({
      component: ModalEntradaComponent, // Se carga el componente de modal
      cssClass: 'custom-modal', // Clase CSS personalizada para darle tamaño al modal
    });

    // Maneja el resultado del modal cuando se cierra
    modal.onDidDismiss().then((result) => {
      if (result.data && result.data.horaEntrada) {
        this.horaEntrada = result.data.horaEntrada; // Asigna la hora seleccionada
        console.log('Hora seleccionada:', this.horaEntrada);
        this.form.get('entrada')?.setValue(this.horaEntrada);
      }
    });

    await modal.present();
  }

  async openModalSalida() {
    const modal = await this.modalController.create({
      component: ModalSalidaComponent, // Se carga el componente de modal
      cssClass: 'custom-modal', // Clase CSS personalizada para darle tamaño al modal
    });

    // Maneja el resultado del modal cuando se cierra
    modal.onDidDismiss().then((result) => {
      if (result.data && result.data.horaSalida) {
        this.horaSalida = result.data.horaSalida; // Asigna la hora seleccionada
        console.log('Hora seleccionada:', this.horaSalida);
        this.form.get('salida')?.setValue(this.horaSalida);
      }
    });

    await modal.present();
  }
  
  cargarDatosDeBaseDeDatos() {
    // Aquí iría una llamada HTTP para obtener datos reales
    this.puestos = [
      { value: 'backend', label: 'Backend Developer' },
      { value: 'frontend', label: 'Frontend Developer' },
      { value: 'tester', label: 'Tester' },
    ];
  }

  registrarHuella() {
    // Aquí iría la lógica para registrar la huella digital
    console.log('Registrando huella...');
    
    // Supongamos que la huella es registrada correctamente
    this.form.get('huellaRegistrada')?.setValue(true);  // Establece que la huella ha sido registrada
  }

 /**
   * Autenticar huella digital en dispositivos móviles
   */
 async authenticateWithFingerprint(): Promise<boolean> {
  try {
    const result = await this.faio.show({
      title: 'Autenticación biométrica',
      subtitle: 'Usa tu huella digital para autenticarte',
      disableBackup: true,
    });
    return result === 'Success';
  } catch (error) {
    console.error('Error en autenticación biométrica:', error);
    return false;
  }
}

/**
 * Registrar huella digital en la web utilizando WebAuthn
 */
async registerFingerprintWeb(): Promise<any> {
  if (!('credentials' in navigator)) {
    throw new Error('WebAuthn no es compatible con este navegador.');
  }

  try {
    const publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions = {
      challenge: new Uint8Array(32), // Generar un desafío seguro desde el servidor
      rp: {
        name: 'Nombre de la aplicación',
      },
      user: {
        id: new Uint8Array(16), // Identificador único del usuario
        name: 'usuario@ejemplo.com',
        displayName: 'Usuario Ejemplo',
      },
      pubKeyCredParams: [
        { alg: -7, type: 'public-key' },
      ],
      authenticatorSelection: {
        authenticatorAttachment: 'platform',
        userVerification: 'required',
      },
      timeout: 60000,
      attestation: 'direct',
    };

    const credential = await navigator.credentials.create({
      publicKey: publicKeyCredentialCreationOptions,
    });

    console.log('Credenciales registradas:', credential);
    return credential;
  } catch (error) {
    console.error('Error en registro de huella biométrica en web:', error);
    throw error;
  }
}

/**
 * Autenticar al usuario en la web utilizando WebAuthn
 */
async authenticateFingerprintWeb(): Promise<any> {
  if (!('credentials' in navigator)) {
    throw new Error('WebAuthn no es compatible con este navegador.');
  }

  try {
    const publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions = {
      challenge: new Uint8Array(32), // Generar un desafío seguro desde el servidor
      allowCredentials: [
        {
          id: new Uint8Array(16), // Identificador de credencial registrado
          type: 'public-key',
        },
      ],
      userVerification: 'required',
      timeout: 60000,
    };

    const assertion = await navigator.credentials.get({
      publicKey: publicKeyCredentialRequestOptions,
    });

    console.log('Autenticación completada:', assertion);
    return assertion;
  } catch (error) {
    console.error('Error en autenticación biométrica en web:', error);
    throw error;
  }
}

async registerFingerprint() {
  try {
    const result = await this.registerFingerprintWeb();
    console.log('Registro exitoso:', result);
  } catch (error) {
    console.error('Error en registro:', error);
  }
}

async authenticateFingerprint() {
  try {
    const isAuthenticated = await this.authenticateWithFingerprint();
    console.log('Autenticación:', isAuthenticated ? 'Exitosa' : 'Fallida');
  } catch (error) {
    console.error('Error en autenticación:', error);
  }
}

//   async show2(){
//   if(await this.faio.isAvailable()){
//    this.faio.show({
//       title: 'Fingerprint-Demo',
//       description: 'password',
//       cancelButtonTitle:'cancel',
//       fallbackButtonTitle:'Use pin',
//       disableBackup: true,
//     })  
//     .then((result: any)=> {

// // almaceno el valor en la variable resultado
// // y la visualizo con {{resultado.withFingerprint}}

//       this.resultado =  result;
//       console.log(result)}
//     )
//     .catch((err: any )=> {
//       this.error = err;
//       console.log(err)}
//     );

//   }
// }
verificarIdentidad ( ) { 
  NativeBiometric.isAvailable().then ( ( isAvailable ) => { 
    if (isAvailable) { 
      NativeBiometric.verifyIdentity ({ 
        reason : 'Para iniciar sesión fácilmente' , 
        title : 'Iniciar sesión' , 
        subtitle : 'Autenticar' , 
        description : 'Por favor, autentíquese para continuar' , 
        maxAttempts : 2 , 
        useFallback : true , 
      }). then ( ( resultado ) => { 
        alert ( "Autenticación biométrica exitosa" ); 
        console . log (resultado); 
      }). catch ( ( error ) => { 
        console . error ( 'Error al verificar la identidad:' , error); 
      }); 
    } else { 
      alert ( 'La autenticación biométrica no está disponible en este dispositivo.' ); 
    } 
  }). catch ( ( e ) => { 
    console . error (e); 
    alert ( 'Autenticación fallida' ); 
  }); 
} 

async onIdentifyButtonClick(): Promise<void> {
  const data = await this.fingerprintService.getVisitorData()
  this.visitorId = data.visitorId
  this.extendedResult = data
}

async authenticate(): Promise<boolean> {
  try {
    const result = await this.faio.show({
      title: 'Registrar Huella Digital',
      subtitle: 'Usa tu lector de huellas',
      disableBackup: true,
      description: 'Coloca tu dedo en el lector para registrar la huella',
    });
    console.log('Huella registrada:', result);
    const data = await this.fingerprintService.getVisitorData()
    this.visitorId = data.visitorId
    this.extendedResult = data
    return true;
  } catch (error) {
    console.error('Error al registrar huella:', error);
    return false;
  }
}

// async addFingerprint() {
//   const nombre: string = this.form.get('nombre')?.value;
//   if (!nombre.trim()) {
//     alert('Por favor, ingresa el nombre del empleado');
//     return;
//   }

//   const isRegistered = await this.authenticate();
//   if (isRegistered) {
//     const fingerprintId = `Huella-${this.fingerprints.length + 1}`;
//     this.fingerprints.push(`${this.employeeName} - ${fingerprintId}`);
//     alert(`Huella registrada para ${this.employeeName}`);
//   } else {
//     alert('Error al registrar la huella');
//   }
// }

async startCamera() {
  try {
    // Verifica si ya hay un stream activo antes de iniciar uno nuevo
    if (this.isCameraOn) {
      console.warn('La cámara ya está encendida.');
      return;
    }

    // Solicitar permisos de cámara
    const stream = await navigator.mediaDevices.getUserMedia({ video: true });

    if (!stream) {
      console.error('No se pudo obtener el flujo de la cámara.');
      return;
    }

    this.isCameraOn = true;
    this.videoElement.nativeElement.srcObject = stream;
    this.videoElement.nativeElement.play();

    // Procesar el video en tiempo real
    this.detectFaces();
  } catch (error) {
    console.error('Error al acceder a la cámara:', error);
  }
}

async detectFaces() {
  const video = this.videoElement.nativeElement;
  const canvas = this.canvas.nativeElement;

  if (!video || !canvas) {
    console.error('El elemento de video o canvas no está disponible.');
    return;
  }

  const displaySize = { width: video.width, height: video.height };
  faceapi.matchDimensions(canvas, displaySize);

  video.addEventListener('play', async () => {
    while (this.isCameraOn) {
      try {
        const detections = await faceapi
          .detectAllFaces(video, new faceapi.TinyFaceDetectorOptions())
          .withFaceLandmarks()
          .withFaceDescriptors();

        if (!detections.length) {
          console.warn('No se detectaron rostros.');
        }

        // Dibujar las detecciones en el canvas
        const resizedDetections = faceapi.resizeResults(detections, displaySize);
        const ctx = canvas.getContext('2d');

        if (ctx) {
          ctx.clearRect(0, 0, canvas.width, canvas.height);
          faceapi.draw.drawDetections(canvas, resizedDetections);
          faceapi.draw.drawFaceLandmarks(canvas, resizedDetections);
        }
        
        await faceapi.tf.nextFrame();
      } catch (error) {
        console.error('Error detectando rostros:', error);
      }
    }
  });
}

stopCamera() {
  try {
    const video = this.videoElement.nativeElement;

    if (!video || !video.srcObject) {
      console.warn('No hay un stream de video activo para detener.');
      return;
    }

    const stream = video.srcObject as MediaStream;
    const tracks = stream.getTracks();

    if (tracks.length === 0) {
      console.warn('No hay pistas activas en el stream.');
      return;
    }

    // Detener las pistas de video
    tracks.forEach(track => track.stop());

    // Limpiar el srcObject del video
    video.srcObject = null;
    this.isCameraOn = false;

    console.log('Cámara detenida correctamente.');
  } catch (error) {
    console.error('Error al detener la cámara:', error);
  }
}

actualizarDias() {
  this.form.patchValue({ dias_trabajo: this.diasSeleccionados });
}

enviarDatos() {
  if (this.form.valid) {
    const formData = this.form.value;
    
    console.log("Datos a enviar:", formData); // Verificar antes de enviar

  //   this.http.post('https://tu-api.com/insertar-usuario', formData)
  //     .subscribe(response => {
  //       console.log('Respuesta del servidor:', response);
  //     }, error => {
  //       console.error('Error al enviar:', error);
  //     });
  // } else {
  //   console.log("Formulario inválido");
  }
}
generarIdEmpleado(ultimoId: string | null): string {
  const anio = new Date().getFullYear().toString().slice(-2); // Últimos 2 dígitos del año

  let secuencia = 1; // Si no hay IDs previos, inicia en 1

  if (ultimoId) {
    const lastNumber = parseInt(ultimoId.substring(2), 10); // Extraer la parte numérica
    secuencia = lastNumber + 1; // Incrementar la secuencia
  }

  return `${anio}${secuencia.toString().padStart(3, '0')}`; // Formato "AA###"
}
  
  obtenerUltimoId() {

    // this.http.post(ConstantesURL.GET_LAST_ID_EMPLEADO, true).toPromise().then(response => {
    //   console.log('response', response);
      



    // }).catch(e => {

    // });
  // return this.http.get<string>(`${this.apiUrl}/ultimoId`);
  // this.http.post(ConstantesURL.UPLOAD_LOGO_PRODUCTO, true).subscribe({
    
  //     next: () => alert('Imagen subida correctamente.'),
  //     error: (err) => alert('Error al subir la imagen: ' + err.message),
    
  //   });

  if(this.id_empleado){
    this.form.patchValue({ id_empleado: this.id_empleado });
  }
}


}

