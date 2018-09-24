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

  private static readonly sportObjectsStorageKey = 'sportObjects';

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  private readonly sportObjectDirectorApi: string =
    `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
  }

  delete(id: string): Observable<void> {
    this.deleteSportObjectFromSession(id);
    return this.http.delete<void>(`${this.sportObjectDirectorApi}/${id}`);
  }

  get(successCallback: (sportObjects: SportObject[]) => void,
      errorCallback: (errorResponse: HttpErrorResponse) => void): void {
    const sportObjects = SportObjectService.tryToGetSportObjectFromSession();

    if (sportObjects) {
      successCallback(sportObjects);
    } else {
      this.http.get<SportObject[]>(this.sportObjectPublicApi).subscribe(
        (sportObjects) => {
          sessionStorage.setItem(SportObjectService.sportObjectsStorageKey, JSON.stringify(sportObjects));
          successCallback(sportObjects);
        },
        (error) => {
          errorCallback(error);
        }
      );
    }
  }

  post(sportObject: SportObject): Observable<SportObject> {
    return this.http.post<SportObject>(this.sportObjectDirectorApi, sportObject);
  }

  put(sportObjectId: string, sportObject: SportObject): Observable<SportObject> {
    return this.http.put<SportObject>(`${this.sportObjectDirectorApi}/${sportObjectId}`, sportObject);
  }

  addSportObjectToSession(sportObject: SportObject) {
    let sportObjects = SportObjectService.tryToGetSportObjectFromSession();
    sportObjects.push(sportObject);
    sessionStorage.setItem(SportObjectService.sportObjectsStorageKey, JSON.stringify(sportObjects));
  }

  updateSportObjectInSession(sportObject: SportObject) {
    let sportObjects = SportObjectService.tryToGetSportObjectFromSession();
    sportObjects = sportObjects.filter(o => o.id !== sportObject.id);
    sportObjects.push(sportObject);
    sessionStorage.setItem(SportObjectService.sportObjectsStorageKey, JSON.stringify(sportObjects));
  }

  deleteSportObjectFromSession(objectId: string) {
    let sportObjects = SportObjectService.tryToGetSportObjectFromSession();
    sportObjects = sportObjects.filter(o => o.id !== objectId);
    sessionStorage.setItem(SportObjectService.sportObjectsStorageKey, JSON.stringify(sportObjects));
  }

  private static tryToGetSportObjectFromSession(): SportObject[] {
    const sportObjects = sessionStorage.getItem(SportObjectService.sportObjectsStorageKey);
    console.log(`tryToGetSportObjectFromSession: ${sportObjects}`);
    if (sportObjects) {
      return JSON.parse(sportObjects);
    } else {
      return null;
    }
  }
}
