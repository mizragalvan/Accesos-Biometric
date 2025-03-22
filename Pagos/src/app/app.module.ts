import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { ComponentsModule } from './components/components.module';
import { ReactiveFormsModule } from '@angular/forms';
import { FingerprintAIO } from '@ionic-native/fingerprint-aio/ngx';
import {FingerprintjsProAngularModule} from '@fingerprintjs/fingerprintjs-pro-angular'
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, IonicModule.forRoot(),FingerprintjsProAngularModule.forRoot({
    loadOptions: {
      apiKey: "uGxOC3CkPpkpOMBUhX4t"
    }
  }), AppRoutingModule,ComponentsModule,ReactiveFormsModule, HttpClientModule],
  providers: [{ provide: RouteReuseStrategy, useClass: IonicRouteStrategy },  FingerprintAIO,],
  bootstrap: [AppComponent],
})
export class AppModule {}
