import {WeekDay} from "@angular/common";
import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export class Time {
  constructor(public hour: number,
              public minute: number) {

  }

  public static of(time: Time): Time {
    return new Time(time.hour, time.minute);
  }

  public static createFromString(str: string): Time {
    const timeArray = str.split(':');
    return new Time(Number(timeArray[0]), Number(timeArray[1]));
  }
}

export class DayOpeningTime {
  constructor(public dayOfWeek: WeekDay,
              public startTime: Time,
              public finishTime: Time,
              public timeInterval: number,
              public price: number ) {}
}

@Injectable()
export class OpeningTimeService {

  private readonly sportObjectDirectorApi: string =
    `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
  }

  post(objectId: string, openingTime: DayOpeningTime): Observable<void> {
    return this.http.post<void>(`${this.sportObjectDirectorApi}/${objectId}/day-opening-time`, openingTime);
  }

  get(objectId: string): Observable<DayOpeningTime[]> {
    return this.http.get<DayOpeningTime[]>(`${this.sportObjectDirectorApi}/${objectId}/day-opening-time`)
  }
}

