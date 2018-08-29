import {Component, Input} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'menu-item',
  templateUrl: './menu-item.component.html',
  styleUrls: ['../menu.component.scss']
})
export class MenuItemComponent {

  @Input() name;
  @Input() path;

  constructor(
    private router: Router) {
  }

  navigate() {
    this.router.navigate([this.path]);
  }
}
