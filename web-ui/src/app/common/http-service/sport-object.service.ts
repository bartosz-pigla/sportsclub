import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Address} from "./sportsclub.service";
import {Observable} from "rxjs";
import {User} from "./user.service";
import {IDeletableService} from "./http-service.service";

export class SportObject {
  constructor(public id: string,
              public sportsclubId: string,
              public name: string,
              public description: string,
              public address: Address,
              public imageUrl: string) {
  }
}

@Injectable()
export class SportObjectService implements IDeletableService<User> {

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  private readonly sportObjectDirectorApi: string =
    `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.sportObjectDirectorApi}/${id}`);
  }

  getOne(objectId: string,
         successCallback: (sportObjects: SportObject) => void,
         errorCallback: (errorResponse: HttpErrorResponse) => void): void {
    this.http.get<SportObject[]>(this.sportObjectPublicApi).subscribe(
      (sportObjects) => {
        successCallback(sportObjects.find(s => s.id === objectId));
      },
      (error) => {
        errorCallback(error);
      }
    )
  }

  get(successCallback: (sportObjects: SportObject[]) => void,
      errorCallback: (errorResponse: HttpErrorResponse) => void): void {
    this.http.get<SportObject[]>(this.sportObjectPublicApi).subscribe(
      (sportObjects) => {
        successCallback(sportObjects);
      },
      (error) => {
        errorCallback(error);
      }
    )  }

  post(sportObject: SportObject): Observable<SportObject> {
    return this.http.post<SportObject>(this.sportObjectDirectorApi, sportObject);
  }

  put(sportObjectId: string, sportObject: SportObject): Observable<SportObject> {
    return this.http.put<SportObject>(`${this.sportObjectDirectorApi}/${sportObjectId}`, sportObject);
  }
}
