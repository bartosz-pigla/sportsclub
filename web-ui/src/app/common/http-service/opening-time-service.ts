import {WeekDay} from "@angular/common";
import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {SportObject} from "./sport-object.service";
import {Observable} from "rxjs";

// export enum DayOfWeek {
//
//   MONDAY = 'MONDAY',
//   TUESDAY = 'TUESDAY',
//   WEDNESDAY = 'WEDNESDAY',
//   THURSDAY = 'THURSDAY',
//   FRIDAY = 'FRIDAY',
//   SATURDAY = 'SATURDAY',
//   SUNDAY = 'SUNDAY'
// }

export class Time {
  constructor(public hour: number,
              public minute: number) {

  }

  public static of(time: Time): Time {
    return new Time(time.hour, time.minute);
  }
  //
  // isBefore(time: Time): boolean {
  //   if (this.hour < time.hour) {
  //     return true;
  //   } else if (this.hour > time.hour) {
  //     return false;
  //   } else {
  //     return this.minute < time.minute;
  //   }
  // }

  getTimeDifference(time: Time): number {
    const hourDifference = (time.hour - this.hour) * 60;
    const minuteDifference = time.minute - this.minute;
    return hourDifference + minuteDifference;
  }

  plusTime(time:Time): Time {
    return new Time(this.hour + time.hour, this.minute + time.minute);
  }

  getMinutes(): number {
    return this.hour * 60 + this.minute;
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

  constructor(private http: HttpClient) {
  }

  get(sportObjectId: string): Observable<OpeningTime[]> {
    return this.http.get<OpeningTime[]>(`${this.sportObjectPublicApi}/${sportObjectId}/opening-time`);
  }
}

