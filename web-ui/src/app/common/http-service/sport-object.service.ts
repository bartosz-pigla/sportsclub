import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Address} from "./sportsclub.service";
import {Observable} from "rxjs";
import {User} from "./user.service";

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
export class SportObjectService {

  private static readonly sportObjectsStorageKey = 'sportObjects';

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  private readonly sportObjectDirectorApi: string =
    `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
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
