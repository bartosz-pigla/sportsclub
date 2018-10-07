import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

export class Statute {
  constructor(public id: string,
              public title: string,
              public description: boolean) {
  }
}

@Injectable()
export class StatuteService {

  private readonly statutePublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/statute`;

  private readonly statuteDirectorApi: string =
    `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/statute`;

  constructor(private http: HttpClient) {
  }

  get(): Observable<Statute> {
    return this.http.get<Statute>(this.statutePublicApi);
  }

  post(statute: Statute): Observable<Statute> {
    return this.http.post<Statute>(this.statuteDirectorApi, statute);
  }
}
