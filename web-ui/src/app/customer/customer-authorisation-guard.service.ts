import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {unauthorizedAbsolutePath} from "../path";

@Injectable()
export class CustomerAuthorisationGuard implements CanActivate {
  constructor(private router: Router) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    this.router.navigate([unauthorizedAbsolutePath]);
    return false;
  }
}
