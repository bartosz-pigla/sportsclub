import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthenticationService} from "../common/security/authentication.service";
import {Observable} from "rxjs";
import {UserType} from "../common/http-service/user.service";

@Injectable({
  providedIn: 'root',
})

export class ReceptionistAuthorizationGuard implements CanActivate {
  constructor(private router: Router,
              private authenticationService: AuthenticationService) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    const userType = this.authenticationService.getUserType();
    return this.authenticationService.isSignedIn() && (userType === UserType.DIRECTOR || userType === UserType.RECEPTIONIST);
  }
}
