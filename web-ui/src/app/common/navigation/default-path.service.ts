import {Injectable} from '@angular/core';
import {AuthenticationService} from "../security/authentication.service";
import {UserType} from "../http-service/user.service";

@Injectable({
  providedIn: 'root',
})
export class DefaultPathService {

  constructor(private authenticationService: AuthenticationService) {
  }

  navigate() {
    if (this.authenticationService.isSignedIn()) {
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
