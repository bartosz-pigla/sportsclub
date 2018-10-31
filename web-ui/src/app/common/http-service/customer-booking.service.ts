import {Injectable} from "@angular/core";
import {getPaginationUrlParams, getSortingUrlParams, IPageableAndSortableGetService, PaginationParams, SortingParams} from "./http-service.service";
import {Booking, BookingDetail, BookingDetailWithOpeningTimeAndPosition} from "./booking.model";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageResponse} from "./page-response";

@Injectable()
export class CustomerBookingService implements IPageableAndSortableGetService<Booking> {

  private readonly sportObjectPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

  private readonly bookingCustomerApi: string =
    `${environment.apiUrl}/customer-api/booking`;

  constructor(private http: HttpClient) {
  }

  get(paginationParams: PaginationParams,
      sortingParams: SortingParams,
      searchParams: Booking): Observable<PageResponse<Booking>> {
    let urlParams = new HttpParams();
    urlParams = getPaginationUrlParams(urlParams, paginationParams);
    urlParams = getSortingUrlParams(urlParams, sortingParams);
    urlParams = CustomerBookingService.getSearchUrlParams(urlParams, searchParams);
    return this.http.get<PageResponse<Booking>>(this.bookingCustomerApi, {params: urlParams});
  }


  getBookingDetail(objectId: string, date: Date): Observable<BookingDetailWithOpeningTimeAndPosition[]> {
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

  cancel(bookingId: string): Observable<void> {
    return this.http.patch<void>(`${this.bookingCustomerApi}/${bookingId}/cancel`, null);
  }

  private static getSearchUrlParams(params: HttpParams, searchParams: Booking): HttpParams {
    return searchParams && searchParams.state ? params.set('state', searchParams.state) : params;
  }
}
//
// @Injectable()
// export class BookingManagementService implements IPageableAndSortableGetService<Booking> {
//
//   private readonly bookingReceptionistApi: string =
//     `${environment.apiUrl}/receptionist-api/booking`;
//
//   constructor(private http: HttpClient) {
//   }
//
//   get(paginationParams: PaginationParams,
//       sortingParams: SortingParams,
//       searchParams: Booking): Observable<PageResponse<Booking>> {
//     let urlParams = new HttpParams();
//     urlParams = getPaginationUrlParams(urlParams, paginationParams);
//     urlParams = getSortingUrlParams(urlParams, sortingParams);
//     urlParams = BookingManagementService.getSearchUrlParams(urlParams, searchParams);
//     return this.http.get<PageResponse<Booking>>(this.bookingReceptionistApi, {params: urlParams});
//   }
//
//   confirm(bookingId: string): Observable<void> {
//     return this.http.patch<void>(`${this.bookingReceptionistApi}/${bookingId}/confirm`, null);
//   }
//
//   finish(bookingId: string): Observable<void> {
//     return this.http.patch<void>(`${this.bookingReceptionistApi}/${bookingId}/finish`, null);
//   }
//
//   reject(bookingId: string): Observable<void> {
//     return this.http.patch<void>(`${this.bookingReceptionistApi}/${bookingId}/reject`, null);
//   }
//
//   private static getSearchUrlParams(params: HttpParams, searchParams: Booking): HttpParams {
//     return searchParams && searchParams.state ? params.set('state', searchParams.state) : params;
//   }
// }
