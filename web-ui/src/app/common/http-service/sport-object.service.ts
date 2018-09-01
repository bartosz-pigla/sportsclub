import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {getUrlParams, PaginationParams, SortingParams} from "./http-service.service";
import {Observable} from "rxjs";
import {PageResponse} from "./page-response";
import {Announcement} from "./announcement-service";

export class Address {
  constructor(public street: string,
              public city: string,
              public latitude: number,
              public longitude: number) {
  }
}

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

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
  }

  get(): Observable<SportObject[]> {
    return this.http.get<SportObject[]>(this.sportObjectPublicApi);
  }
}
