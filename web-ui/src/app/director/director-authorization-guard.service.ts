import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from "../common/security/authentication.service";
import {UserType} from "../common/http-service/user.service";

@Injectable({
  providedIn: 'root',
})

export class DirectorAuthorizationGuard implements CanActivate {
  constructor(private router: Router,
              private authenticationService: AuthenticationService) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.authenticationService.isSignedIn() && this.authenticationService.getUserType() == UserType.DIRECTOR;
  }
}
