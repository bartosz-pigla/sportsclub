import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {User, UserType} from "../http-service/user.service";

export class CurrentUserDetails {
  constructor(public token,
              public user: User) {
  }
}

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  private currentUserStorageKey = "currentUser";

  constructor(private http: HttpClient) {
  }

  signIn(username, password, success: () => void, fail: () => void) {
    if(this.isSignedIn()) {
      this.signOut();
    }

    let url = `${environment.apiUrl}/public-api/sign-in`;
    let body = {username: username, password: password};

    this.http.post<CurrentUserDetails>(url, body).subscribe(
      (data: CurrentUserDetails) => {
        localStorage.setItem(this.currentUserStorageKey, JSON.stringify(data));
        success();
      },
      () => {
        fail();
      });
  }

  signOut() {
    localStorage.removeItem(this.currentUserStorageKey);
  }

  isSignedIn() {
    return localStorage.getItem(this.currentUserStorageKey) !== null;
  }

  getToken() {
    return this.getDetails().token;
  }

  getUserType(): UserType {
    return this.getDetails().user.userType;
  }

  getUsername() {
    return this.getDetails().user.username;
  }

  getDetails(): CurrentUserDetails {
    return JSON.parse(localStorage.getItem(this.currentUserStorageKey));
  }
}
