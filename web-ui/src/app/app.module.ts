import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {SignInComponent} from './public/sign-in/sign-in.component';
import {HomeComponent} from './customer/home/home.component';
import {AppRoutingModule} from './app-routing.module';
import {CustomerAuthorisationGuard} from "./customer/customer-authorisation-guard.service";
import {NotFoundComponent} from './public/not-found/not-found.component';
import {UnauthorizedComponent} from './public/unauthorized/unauthorized.component';
import {PublicComponent} from './public/public.component';
import {CustomerComponent} from './customer/customer.component';

@NgModule({
  declarations: [
    AppComponent,
    SignInComponent,
    HomeComponent,
    NotFoundComponent,
    UnauthorizedComponent,
    PublicComponent,
    CustomerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [CustomerAuthorisationGuard],
  bootstrap: [AppComponent]
})
export class AppModule {
}
