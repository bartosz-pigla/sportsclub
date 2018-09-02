import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {NotFoundComponent} from './public/not-found/not-found.component';
import {UnauthorizedComponent} from './public/unauthorized/unauthorized.component';
import {PublicComponent} from './public/public.component';
import {CustomerComponent} from './customer/customer.component';
import {MatButtonModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule} from "@angular/material";

import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HttpClient, HttpClientModule} from '@angular/common/http';
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
import { UserFormComponent } from './common/form/user-form/user-form.component';
import { BackendErrorsComponent } from './common/form/backend-errors/backend-errors.component';

const MATERIAL_MODULES = [
  MatButtonModule,
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatSelectModule
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
    UserManagementComponent,
    UserFormComponent,
    BackendErrorsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MATERIAL_MODULES,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    UndefinedErrorDialog,
    NewItemButtonComponent,
    ListViewComponent,
    AnnouncementFormComponent,
    DeleteItemComponent,
    ConfirmationDialog]
})
export class AppModule {
}
