import {Injectable} from '@angular/core';
import {AuthenticationService} from "../security/authentication.service";
import {UserType} from "../user.service";

@Injectable({
  providedIn: 'root',
})
export class DefaultPathService {

  constructor(private authenticationService: AuthenticationService) {
  }

  navigate() {
    if (this.authenticationService.isSignedIn()) {
      let userType: string = this.authenticationService.getUserType().toString();

      switch (this.authenticationService.getUserType()) {
        case UserType.CUSTOMER:
          return 'public';
        case UserType.DIRECTOR:
          return 'director';
        case UserType.RECEPTIONIST:
          return 'public/home';
        default:
          return 'public/home';
      }
    } else {
      return 'public/home';
    }
  }
}
