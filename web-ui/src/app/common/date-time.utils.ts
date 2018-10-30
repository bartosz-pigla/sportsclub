import {SessionDate} from "./booking-summary.service";

export const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];

export function addDayToDate(previousDate: Date, day: number) {
  return new Date(previousDate.getTime() + day * 24 * 60 * 60 * 1000);
}

export function currentDate() {
  let currentDate = new Date();
  currentDate.setHours(0,0,0,0);
  return currentDate;
}

export function dateEquals(date1: Date, date2: Date) {
  return date1.getDate() === date2.getDate() && date1.getMonth() === date2.getMonth() && date1.getFullYear() === date2.getFullYear();
}

export function createFromSessionDate(sessionDate: SessionDate): Date {
  return new Date(sessionDate.year, sessionDate.month, sessionDate.day, 0, 0, 0, 0);
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
