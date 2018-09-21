import {OpeningTime, Time} from "./http-service/opening-time-service";
import {WeekDay} from "@angular/common";

// export enum OpeningTimeGenerationType {
//
//   WEEKDAYS,
//   SATURDAY,
//   SUNDAY
// }

export function generateOpeningTimesForDay(day: WeekDay,
                                          startTime: Time,
                                          finishTime: Time,
                                          interval: Time,
                                          price: number): OpeningTime[] {
  console.log(`
    day: ${JSON.stringify(day)}
    startTime: ${JSON.stringify(startTime)}
    finishTime: ${JSON.stringify(finishTime)}
    interval: ${JSON.stringify(interval)}
    price: ${JSON.stringify(price)}
  `);

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

export function getStartTimes(openingTimes: OpeningTime[]): Set<Time> {
  return new Set(openingTimes
    .sort((o1, o2) => {
      const timeDifference = o1.startTime.getTimeDifference(o2.startTime);
      if (timeDifference > 0) {
        return 1;
      } else if (timeDifference < 0) {
        return -1;
      } else {
        return 0
      }
    })
    .map(openingTime => openingTime.startTime));
}
