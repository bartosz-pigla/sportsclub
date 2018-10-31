import {Component} from '@angular/core';
import {AuthenticationService} from "../../security/authentication.service";
import {Router} from "@angular/router";
import {UserType} from "../../http-service/user.service";

@Component({
  selector: 'menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

  constructor(
    private router: Router,
    public authenticationService: AuthenticationService) {
  }

  signOut(): void {
    this.authenticationService.signOut();
    this.router.navigate(['public/sign-in']);
  }

  customerIsSignedIn() {
    return this.authenticationService.getUserType() === UserType.CUSTOMER;
  }
}
