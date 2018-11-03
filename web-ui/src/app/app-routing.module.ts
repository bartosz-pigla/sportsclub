import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {PublicComponent} from "./public/public.component";
import {UnauthorizedComponent} from "./public/unauthorized/unauthorized.component";
import {NotFoundComponent} from "./public/not-found/not-found.component";
import {SignInComponent} from "./public/sign-in/sign-in.component";
import {SportObjectComponent} from "./public/sport-object/sport-object.component";
import {DirectorComponent} from "./director/director.component";
import {DirectorAuthorizationGuard} from "./director/director-authorization-guard.service";
import {DirectorHomeComponent} from "./director/director-home/director-home.component";
import {PublicHomeComponent} from "./public/public-home/public-home.component";
import {UserManagementComponent} from "./director/user-management/user-management.component";
import {SportObjectManagementComponent} from "./director/sport-object-management/sport-object-management.component";
import {StatuteManagementComponent} from "./director/statute-management/statute-management.component";
import {CustomerComponent} from "./customer/customer.component";
import {CustomerAuthorizationGuard} from "./customer/customer-authorization-guard.service";
import {CustomerBookingsComponent} from "./customer/customer-bookings/customer-bookings.component";
import {ReceptionistComponent} from "./receptionist/receptionist.component";
import {ReceptionistAuthorizationGuard} from "./receptionist/receptionist-authorization-guard.service";
import {BookingManagementComponent} from "./receptionist/booking-management/booking-management.component";
import {SignUpComponent} from "./public/sign-up/sign-up.component";
import {StatuteComponent} from "./public/statute/statute.component";
import {CustomerActivationComponent} from "./public/customer-activation/customer-activation.component";

const routes: Routes = [
  {path: '', redirectTo: 'public/home', pathMatch: 'full'},
  {path: '**', redirectTo: 'not-found', pathMatch: 'full'},

  {
    path: 'public', component: PublicComponent, children: [
      {path: '', redirectTo: 'home', pathMatch: 'prefix'},
      {path: 'home', component: PublicHomeComponent},
      {path: 'sign-in', component: SignInComponent},
      {path: 'sign-up', component: SignUpComponent},
      {path: 'sport-object/:id', component: SportObjectComponent},
      {path: 'statute', component: StatuteComponent},
      {path: 'customer-activation/:id', component: CustomerActivationComponent},
      {path: 'unauthorized', component: UnauthorizedComponent},
      {path: 'not-found', component: NotFoundComponent}
    ]
  },
  {
    path: 'customer', component: CustomerComponent, canActivate: [CustomerAuthorizationGuard], children: [
      {path: '', redirectTo: 'bookings', pathMatch: 'prefix'},
      {path: 'bookings', component: CustomerBookingsComponent}
    ]
  },
  {
    path: 'receptionist', component: ReceptionistComponent, canActivate: [ReceptionistAuthorizationGuard], children: [
      {path: '', redirectTo: 'booking-management', pathMatch: 'prefix'},
      {path: 'booking-management', component: BookingManagementComponent}
    ]
  },
  {
    path: 'director', component: DirectorComponent, canActivate: [DirectorAuthorizationGuard], children: [
      {path: '', redirectTo: 'home', pathMatch: 'prefix'},
      {path: 'home', component: DirectorHomeComponent},
      {path: 'user-management', component: UserManagementComponent},
      {path: 'sport-object-management', component: SportObjectManagementComponent},
      {path: 'statute-management', component: StatuteManagementComponent}
    ]
  }
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule {
}
