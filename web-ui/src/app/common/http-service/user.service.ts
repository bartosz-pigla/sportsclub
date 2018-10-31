import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {
  getPaginationUrlParams,
  getSortingUrlParams,
  IDeletableService,
  IPageableAndSortableGetService,
  PaginationParams,
  SortingParams
} from "./http-service.service";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {PageResponse} from "./page-response";

export enum UserType {
  CUSTOMER = "CUSTOMER",
  RECEPTIONIST = "RECEPTIONIST",
  DIRECTOR = "DIRECTOR"
}

export class User {
  constructor(public id?: string,
              public username?: string,
              public userType?: UserType,
              public email?: string,
              public phoneNumber?: string,
              public activated?: boolean,
              public deleted?: boolean) {
  }
}

@Injectable()
export class UserService implements IPageableAndSortableGetService<User>, IDeletableService<User> {

  private readonly userReceptionistApi: string =
    `${environment.apiUrl}/receptionist-api/user`;

  private readonly userDirectorApi: string =
    `${environment.apiUrl}/director-api/user`;

  private readonly customerDirectorApi: string =
    `${environment.apiUrl}/director-api/customer`;

  private readonly receptionistDirectorApi: string =
    `${environment.apiUrl}/director-api/receptionist`;

  private readonly directorDirectorApi: string =
    `${environment.apiUrl}/director-api/director`;

  constructor(private http: HttpClient) {
  }

  delete(id: string): Observable<void> {
    return undefined;
  }

  get(paginationParams: PaginationParams, sortingParams: SortingParams, searchParams: User): Observable<PageResponse<User>> {
    let urlParams = new HttpParams();
    urlParams = getPaginationUrlParams(urlParams, paginationParams);
    urlParams = getSortingUrlParams(urlParams, sortingParams);
    urlParams = UserService.getSearchUrlParams(urlParams, searchParams);

    return this.http.get<PageResponse<User>>(this.userReceptionistApi, {params: urlParams});
  }

  postCustomer(customer: User): Observable<User> {
    return this.http.post<User>(this.customerDirectorApi, customer);
  }

  postReceptionist(receptionist: User): Observable<User> {
    return this.http.post<User>(this.receptionistDirectorApi, receptionist);
  }

  postDirector(director: User): Observable<User> {
    return this.http.post<User>(this.directorDirectorApi, director);
  }

  activate(userId: string, activated: boolean): Observable<void> {
    return this.http.patch<void>(`${this.userDirectorApi}/${userId}/activate`, {activated: activated});
  }

  private static getSearchUrlParams(params: HttpParams, searchParams: User): HttpParams {
    if (searchParams) {
      if (searchParams.username) {
        params = params.set('username', searchParams.username)
      }

      if (searchParams.email) {
        params = params.set('email', searchParams.email);
      }

      if (searchParams.phoneNumber) {
        params = params.set('phoneNumber', searchParams.phoneNumber);
      }

      if (searchParams.userType) {
        params = params.set('userType', searchParams.userType);
      }

      if (searchParams.activated) {
        params = params.set('activated', String(searchParams.activated));
      }
    }

    return params;
  }
}
