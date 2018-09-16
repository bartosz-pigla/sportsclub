import {OpeningTime, Time} from "./http-service/opening-time-service";
import {WeekDay} from "@angular/common";

// export enum OpeningTimeGenerationType {
//
//   WEEKDAYS,
//   SATURDAY,
//   SUNDAY
// }

export function generateOpeningTimeForDay(day: WeekDay,
                                          startTime: Time,
                                          finishTime: Time,
                                          interval: Time,
                                          price: number): OpeningTime[] {
  let openingTimes: OpeningTime[] = [];
  let currentTime = Time.of(startTime);
  let nextTime: Time;

  while (currentTime.getTimeDifference(finishTime) >= interval.getMinutes()) {
    nextTime = currentTime.plusTime(interval);
    openingTimes.push(new OpeningTime(null, day, currentTime, nextTime, price));
    currentTime = nextTime;
  }

  return openingTimes;
}
