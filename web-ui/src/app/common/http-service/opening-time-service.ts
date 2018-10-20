import {WeekDay} from "@angular/common";
import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];

// export function addDayToDate(date: Date, day: number) {
//   return new Date(date.getDate() + day);
// }

export function addDayToDate(previousDate: Date, day: number) {
  return new Date(previousDate.getTime() + day * 24 * 60 * 60 * 1000);
}

export function currentDate() {
  let currentDate = new Date();
  return currentDate;
  // return currentDate.getUTCDate();
}

// function getDateWithoutTime(date: Date) {
//   date.setHours(3,0,0,0);
//   return date;
// }

export function dateEquals(date1: Date, date2: Date) {
  console.log(`date1: ${typeof date1} date2: ${typeof date2}`);
  return date1.getDate() === date2.getDate() && date1.getMonth() === date2.getMonth() && date1.getFullYear() === date2.getFullYear();
}

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

