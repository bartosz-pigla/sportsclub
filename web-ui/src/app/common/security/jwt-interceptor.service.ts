import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authenticationService.isSignedIn()) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authenticationService.getToken()}`
        }
      });
    }

    return next.handle(request);
  }
}
