import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {NotFoundComponent} from './public/not-found/not-found.component';
import {UnauthorizedComponent} from './public/unauthorized/unauthorized.component';
import {PublicComponent} from './public/public.component';
import {CustomerComponent} from './customer/customer.component';
import {MatButtonModule, MatCardModule, MatDialogModule, MatDividerModule, MatFormFieldModule, MatInputModule} from "@angular/material";

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
import {ConnectionErrorDialog} from './common/component/connection-error-dialog/connection-error-dialog.component';
import {DirectorMenuComponent} from './director/director-menu/director-menu.component';
import {PublicHomeComponent} from "./public/public-home/public-home.component";
import {DirectorHomeComponent} from "./director/director-home/director-home.component";
import { NewItemButtonComponent } from './common/component/new-item-button/new-item-button.component';
import {PaginationComponent} from "./common/component/pagination/pagination.component";

const MATERIAL_MODULES = [
  MatButtonModule,
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule
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
    PaginationComponent,
    AnnouncementItemComponent,
    ConnectionErrorDialog,
    DirectorMenuComponent,
    DirectorHomeComponent,
    NewItemButtonComponent,
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
  entryComponents: [ConnectionErrorDialog, NewItemButtonComponent, PaginationComponent]
})
export class AppModule {
}
