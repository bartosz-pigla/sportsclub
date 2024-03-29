import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export class SportObjectPosition {
  constructor(public id: string,
              public name: string,
              public description: string,
              public positionsCount: number) {
  }
}

@Injectable()
export class SportObjectPositionService {

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  private readonly sportObjectDirectorApi: string =
    `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
  }

  get(objectId: string): Observable<SportObjectPosition[]> {
    return this.http.get<SportObjectPosition[]>(`${this.sportObjectPublicApi}/${objectId}/sport-object-position`);
  }

  post(objectId: string, position: SportObjectPosition): Observable<SportObjectPosition> {
    return this.http.post<SportObjectPosition>(`${this.sportObjectDirectorApi}/${objectId}/sport-object-position`, position);
  }

  delete(objectId: string, positionId: string): Observable<void> {
    return this.http.delete<void>(`${this.sportObjectDirectorApi}/${objectId}/sport-object-position/${positionId}`);
  }
}
