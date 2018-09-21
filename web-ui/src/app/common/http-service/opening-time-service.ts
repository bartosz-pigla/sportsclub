import {WeekDay} from "@angular/common";
import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
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

  public static createFromMinutes(minutes: number): Time {
    return new Time(Math.floor(minutes / 60), minutes % 60);
  }

  public static createFromString(str: string): Time {
    const hourMinuteArray = str.split(':');
    return new Time(Number(hourMinuteArray[0]), Number(hourMinuteArray[1]));
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
    console.log(`time: ${time.hour}`);
    console.log(`thistime: ${this.hour}`);

    const hourDifference = (time.hour - this.hour) * 60;
    const minuteDifference = time.minute - this.minute;

    console.log(`time difference: ${hourDifference + minuteDifference}`);

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

