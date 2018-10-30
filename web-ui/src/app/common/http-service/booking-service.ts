import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {dateEquals, Time} from "../date-time.utils";

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

  static createFrom(detail: BookingDetailWithOpeningTimeAndPosition, date: Date) {
    return new BookingDetail('', detail.positionId, detail.openingTimeId, date);
  }

  equals(detail: BookingDetail) {
    return this.sportObjectPositionId === detail.sportObjectPositionId &&
      this.openingTimeId === detail.openingTimeId &&
      dateEquals(this.date, detail.date);
  }
}

export class Booking {
  constructor(
    public id: string,
    public customerId: string,
    public date: string,
    public state: BookingState) {
  }
}

export enum BookingState {
  CREATED = "CREATED",
  SUBMITTED = "SUBMITTED",
  CONFIRMED = "CONFIRMED",
  CANCELED = "CANCELED",
  REJECTED = "REJECTED",
  FINISHED = "FINISHED"
}

@Injectable()
export class BookingService {

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  private readonly bookingCustomerApi: string =
    `${environment.apiUrl}/customer-api/booking`;

  constructor(private http: HttpClient) {
  }

  get(objectId: string, date: Date): Observable<BookingDetailWithOpeningTimeAndPosition[]> {
    let urlParams = (new HttpParams()).set('date', date.toDateString());
    return this.http.get<BookingDetailWithOpeningTimeAndPosition[]>(`${this.sportObjectPublicApi}/${objectId}/opening-times-with-bookings`, {params: urlParams});
  }

  create(): Observable<Booking> {
    return this.http.post<Booking>(this.bookingCustomerApi, null);
  }

  addDetail(bookingId: string, detail: BookingDetail): Observable<BookingDetail> {
    return this.http.post<BookingDetail>(`${this.bookingCustomerApi}/${bookingId}/detail`, {
      sportObjectPositionId: detail.sportObjectPositionId,
      openingTimeId: detail.openingTimeId,
      date: detail.date.toDateString()
    });
  }

  submit(bookingId: string): Observable<void> {
    return this.http.patch<void>(`${this.bookingCustomerApi}/${bookingId}/submit`, null);
  }
}
