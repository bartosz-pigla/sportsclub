import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

export enum UserType {
  CUSTOMER = "CUSTOMER",
  RECEPTIONIST = "RECEPTIONIST",
  DIRECTOR = "DIRECTOR"
}

export class User {
  constructor(public id: string,
              public username: string,
              public userType: UserType,
              public email: string,
              public phoneNumber: string,
              public activated: boolean,
              public deleted: boolean) {
  }
}

@Injectable()
export class UserService {

  constructor(private http: HttpClient) {
  }
}
