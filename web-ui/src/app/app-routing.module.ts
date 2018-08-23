import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {customerPath, homeRelativePath, notFoundRelativePath, publicPath, signInAbsolutePath, signInRelativePath, unauthorizedRelativePath} from "./path";
import {PublicComponent} from "./public/public.component";
import {SignInComponent} from "./public/sign-in/sign-in.component";
import {UnauthorizedComponent} from "./public/unauthorized/unauthorized.component";
import {NotFoundComponent} from "./public/not-found/not-found.component";
import {CustomerComponent} from "./customer/customer.component";
import {HomeComponent} from "./customer/home/home.component";
import {CustomerAuthorisationGuard} from "./customer/customer-authorisation-guard.service";

const routes: Routes = [
  // {path: homePagePath, component: HomeComponent, canActivate: [CustomerAuthorisationGuard]},
  // {path: '', component: SignInComponent},
  // {path: signInAbsolutePath, component: SignInComponent},
  // {path: unauthorizedAbsolutePath, component: UnauthorizedComponent},
  // {path: '**', component: NotFoundComponent}

  // {path: '', redirectTo: signInAbsolutePath, pathMatch: 'full'},
  // {
  //   path: publicPath, component: PublicComponent, children: [
  //     {path: '', redirectTo: signInRelativePath},
  //     {path: signInRelativePath, component: SignInComponent},
  //     {path: unauthorizedRelativePath, component: UnauthorizedComponent},
  //     {path: notFoundRelativePath, component: NotFoundComponent}
  //   ]
  // },
  // {
  //   path: customerPath, component: CustomerComponent, children: [
  //     {path: '', redirectTo: homeRelativePath},
  //     {path: homeRelativePath, component: HomeComponent}
  //   ]
  // }


  {path: '', redirectTo: signInAbsolutePath, pathMatch: 'full'},
  {
    path: publicPath, component: PublicComponent, children: [
      {path: '', redirectTo: signInRelativePath, pathMatch: 'prefix'},
      {path: signInRelativePath, component: SignInComponent},
      {path: unauthorizedRelativePath, component: UnauthorizedComponent},
      {path: notFoundRelativePath, component: NotFoundComponent}
    ]
  },
  {
    path: customerPath, component: CustomerComponent, canActivate: [CustomerAuthorisationGuard], children: [
      {path: '', redirectTo: homeRelativePath, pathMatch: 'prefix'},
      {path: homeRelativePath, component: HomeComponent}
    ]
  }
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule {
}
