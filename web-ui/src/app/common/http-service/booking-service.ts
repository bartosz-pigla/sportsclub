import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {dateEquals, Time} from "./opening-time-service";

export class BookingDetailWithOpeningTimeAndPosition {
  constructor(
    public positionId: string,
    public name: string,
    public bookedPositionsCount: number,
    public positionsCount: number,
    public openingTimeId: string,
    public startTime: Time,
    public finishTime: Time,
    public price: number) {
  }
}

export class BookingDetail {
  constructor(
    public bookingDetailId: string,
    public sportObjectPositionId: string,
    public openingTimeId: string,
    public date: Date) {
  }

  static createFrom(detail: BookingDetailWithOpeningTimeAndPosition , date: Date) {
    return new BookingDetail('', detail.positionId, detail.openingTimeId, date);
  }

  equals(detail: BookingDetail) {
    return this.sportObjectPositionId === detail.sportObjectPositionId &&
      this.openingTimeId === detail.openingTimeId &&
      dateEquals(this.date, detail.date);
  }
}

@Injectable()
export class BookingService {

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  constructor(private http: HttpClient) {
  }

  get(objectId: string, date: Date): Observable<BookingDetailWithOpeningTimeAndPosition[]> {
    let urlParams = (new HttpParams()).set('dateTime', date.toUTCString());
    return this.http.get<BookingDetailWithOpeningTimeAndPosition[]>(`${this.sportObjectPublicApi}/${objectId}/opening-times-with-bookings`, {params: urlParams});
  }
}
