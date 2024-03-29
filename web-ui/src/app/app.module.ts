import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {NotFoundComponent} from './public/not-found/not-found.component';
import {UnauthorizedComponent} from './public/unauthorized/unauthorized.component';
import {PublicComponent} from './public/public.component';
import {CustomerComponent} from './customer/customer.component';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatSelectModule,
  MatStepperModule
} from "@angular/material";

import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ReactiveFormsModule} from "@angular/forms";
import {SignInComponent} from './public/sign-in/sign-in.component';
import {DirectorComponent} from './director/director.component';
import {MenuComponent} from './common/component/menu/menu.component';
import {MenuItemComponent} from "./common/component/menu/menu-item/menu-item.component";
import {PublicMenuComponent} from "./public/menu/public-menu.component";
import {SportObjectComponent} from './public/sport-object/sport-object.component';
import {AnnouncementItemComponent} from './common/component/announcement-item/announcement-item.component';
import {UndefinedErrorDialog} from './common/dialog/undefined-error/undefined-error.dialog';
import {DirectorMenuComponent} from './director/director-menu/director-menu.component';
import {PublicHomeComponent} from "./public/public-home/public-home.component";
import {DirectorHomeComponent} from "./director/director-home/director-home.component";
import {NewItemButtonComponent} from './common/component/new-item-button/new-item-button.component';
import {ListViewComponent} from "./common/component/list-view/list-view.component";
import {AnnouncementFormComponent} from './common/form/announcement-form/announcement-form.component';
import {DeleteItemComponent} from './common/component/delete-item/delete-item.component';
import {ConfirmationDialog} from './common/dialog/confirmation/confirmation.dialog';
import {UserManagementComponent} from './director/user-management/user-management.component';
import {UserFormComponent} from './common/form/user-form/user-form.component';
import {BackendErrorsComponent} from './common/form/backend-errors/backend-errors.component';
import {ErrorDialog} from "./common/dialog/error/error.dialog";
import {SportObjectManagementComponent} from './director/sport-object-management/sport-object-management.component';
import {AgmCoreModule, GoogleMapsAPIWrapper} from "@agm/core";
import {SportObjectCreatorComponent} from "./common/form/sport-object-creator/sport-object-creator.component";
import {CurrencyMaskModule} from "ng2-currency-mask";
import {StatuteManagementComponent} from './director/statute-management/statute-management.component';
import {SignInDialog} from "./common/dialog/sign-in/sign-in.dialog";
import {BookingSummaryComponent} from './common/component/booking-summary/booking-summary.component';
import {BookingSuccessDialog} from './common/dialog/booking-success/booking-success.dialog';
import {JwtInterceptor} from "./common/security/jwt-interceptor.service";
import {CustomerBookingsComponent} from './customer/customer-bookings/customer-bookings.component';
import {BookingItemComponent} from './common/component/booking-item/booking-item.component';
import {ReceptionistComponent} from './receptionist/receptionist.component';
import {BookingManagementComponent} from './receptionist/booking-management/booking-management.component';
import {SignUpComponent} from './public/sign-up/sign-up.component';
import {RecaptchaModule} from "ng-recaptcha";
import {RecaptchaFormsModule} from "ng-recaptcha/forms";
import {ActivationLinkSentDialog} from './common/dialog/activation-link-sent/activation-link-sent.dialog';
import {UsernameAlreadyExistsDialog} from './common/dialog/username-already-exists/username-already-exists.dialog';
import {AbstractInfoDialog} from "./common/dialog/abstract-info/abstract-info.dialog";
import { StatuteComponent } from './public/statute/statute.component';
import { CustomerActivationComponent } from './public/customer-activation/customer-activation.component';

const MATERIAL_MODULES = [
  MatButtonModule,
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatSelectModule,
  MatStepperModule,
  MatIconModule
];

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    PublicHomeComponent,
    NotFoundComponent,
    UnauthorizedComponent,
    PublicComponent,
    CustomerComponent,
    SignInComponent,
    DirectorComponent,
    PublicMenuComponent,
    MenuComponent,
    MenuItemComponent,
    SportObjectComponent,
    ListViewComponent,
    AnnouncementItemComponent,
    UndefinedErrorDialog,
    DirectorMenuComponent,
    DirectorHomeComponent,
    NewItemButtonComponent,
    AnnouncementFormComponent,
    DeleteItemComponent,
    ConfirmationDialog,
    ErrorDialog,
    SignInDialog,
    BookingSuccessDialog,
    UserManagementComponent,
    UserFormComponent,
    BackendErrorsComponent,
    SportObjectManagementComponent,
    SportObjectCreatorComponent,
    StatuteManagementComponent,
    BookingSummaryComponent,
    CustomerBookingsComponent,
    BookingItemComponent,
    ReceptionistComponent,
    BookingManagementComponent,
    SignUpComponent,
    ActivationLinkSentDialog,
    UsernameAlreadyExistsDialog,
    AbstractInfoDialog,
    StatuteComponent,
    CustomerActivationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MATERIAL_MODULES,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    CurrencyMaskModule,
    HttpClientModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
    AgmCoreModule.forRoot({apiKey: 'AIzaSyAyCKx-obJNhZmaTt3Qf5KwmpFd0kmhsQs'})
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    UndefinedErrorDialog,
    NewItemButtonComponent,
    ListViewComponent,
    AnnouncementFormComponent,
    BookingSummaryComponent,
    DeleteItemComponent,
    ConfirmationDialog,
    ErrorDialog,
    SignInDialog,
    BookingSuccessDialog,
    ActivationLinkSentDialog,
    UsernameAlreadyExistsDialog,
    AbstractInfoDialog
  ],
  providers: [
    GoogleMapsAPIWrapper,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}
  ]
})
export class AppModule {
}
