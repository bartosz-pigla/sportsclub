import {Component, OnInit} from '@angular/core';
import {MenuItem} from "../../common/component/menu/menu-item/menu-item.model";
import {SportObjectService} from "../../common/sport-object.service";
import {ActivatedRoute, Router} from "@angular/router";
import {BaseComponent} from "../../common/base.component";
import {MatDialog} from "@angular/material";
import {PreviousPageService} from "../../common/navigation/previous-page.service";

@Component({
  selector: 'public-menu',
  templateUrl: './public-menu.component.html',
  styleUrls: ['./public-menu.component.scss'],
  providers: [SportObjectService]
})
export class PublicMenuComponent extends BaseComponent implements OnInit{

  menuItems: MenuItem[] = [];

  constructor(private sportObjectService: SportObjectService,
              private activatedRoute: ActivatedRoute,
              private previousPageService: PreviousPageService,
              dialog: MatDialog) {
    super(dialog);
  }

  ngOnInit() {
    this.sportObjectService.get(
      (sportObjects) => {
        sportObjects.forEach((object) => {
          this.menuItems.push(new MenuItem(object.name, `public/sport-object/${object.id}`));
        });
      },
      () => {
        this.openConnectionErrorDialog();
      }
    );
  }
}
