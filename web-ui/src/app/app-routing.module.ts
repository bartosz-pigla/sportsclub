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

const routes: Routes = [
  {path: '', redirectTo: 'public/home', pathMatch: 'full'},
  {path: '**', redirectTo: 'not-found', pathMatch: 'full'},

  {
    path: 'public', component: PublicComponent, children: [
      {path: '', redirectTo: 'home', pathMatch: 'prefix'},
      {path: 'home', component: PublicHomeComponent},
      {path: 'sign-in', component: SignInComponent},
      {path: 'sport-object/:id', component: SportObjectComponent},
      {path: 'unauthorized', component: UnauthorizedComponent},
      {path: 'not-found', component: NotFoundComponent}
    ]
  },

  {
    path: 'director', component: DirectorComponent, canActivate: [DirectorAuthorizationGuard], children: [
      {path: '', redirectTo: 'home', pathMatch: 'prefix'},
      {path: 'home', component: DirectorHomeComponent},
      {path: 'user-management', component: UserManagementComponent}
    ]
  }
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule {
}
