import {Component, OnInit} from '@angular/core';
import {MenuItem} from "../../common/component/menu/menu-item/menu-item.model";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material";
import {PreviousPageService} from "../../common/navigation/previous-page.service";
import {SportObjectService} from "../../common/http-service/sport-object.service";
import {ErrorHandlerService} from "../../common/error-handler.service";
import {HttpErrorResponse} from "@angular/common/http";
import {AuthenticationService} from "../../common/security/authentication.service";

@Component({
  selector: 'public-menu',
  templateUrl: './public-menu.component.html',
  providers: [SportObjectService]
})
export class PublicMenuComponent implements OnInit {

  menuItems: MenuItem[];

  private readonly handleError = (error: HttpErrorResponse) => {
    this.errorHandlerService.showDialog(this.dialog, error);
  };

  constructor(private sportObjectService: SportObjectService,
              private activatedRoute: ActivatedRoute,
              private previousPageService: PreviousPageService,
              private dialog: MatDialog,
              private errorHandlerService: ErrorHandlerService,
              public authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.sportObjectService.get(
      (sportObjects) => {
        this.menuItems = [];
        sportObjects.forEach((object) => {
          this.menuItems.push(new MenuItem(object.name, `public/sport-object/${object.id}`));
        });
      },
      (error) => {
        this.handleError(error);
      }
    );
  }
}
