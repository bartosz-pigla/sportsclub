import {WeekDay} from "@angular/common";
import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Time} from "../date-time.utils";

export class DayOpeningTime {
  constructor(public dayOfWeek: WeekDay,
              public startTime: Time,
              public finishTime: Time,
              public timeInterval: number,
              public price: number) {
  }
}

export class OpeningTime {
  constructor(public id: string,
              public dayOfWeek: WeekDay,
              public startTime: Time,
              public finishTime: Time,
              public price: number) {
  }
}

@Injectable()
export class OpeningTimeService {

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  private readonly sportObjectDirectorApi: string =
    `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
  }

  post(objectId: string, openingTime: DayOpeningTime): Observable<void> {
    return this.http.post<void>(`${this.sportObjectDirectorApi}/${objectId}/day-opening-time`, openingTime);
  }

  getOpeningTimes(objectId: string): Observable<OpeningTime[]> {
    return this.http.get<OpeningTime[]>(`${this.sportObjectPublicApi}/${objectId}/opening-time`);
  }

  getDayOpeningTimes(objectId: string): Observable<DayOpeningTime[]> {
    return this.http.get<DayOpeningTime[]>(`${this.sportObjectDirectorApi}/${objectId}/day-opening-time`);
  }
}

