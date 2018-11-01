import {Injectable} from "@angular/core";
import {getPaginationUrlParams, getSortingUrlParams, IPageableAndSortableGetService, PaginationParams, SortingParams} from "./http-service.service";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageResponse} from "./page-response";
import {Booking} from "./booking.model";

@Injectable()
export class BookingManagementService implements IPageableAndSortableGetService<Booking> {

  private readonly bookingReceptionistApi: string =
    `${environment.apiUrl}/receptionist-api/booking`;

  constructor(private http: HttpClient) {
  }

  get(paginationParams: PaginationParams,
      sortingParams: SortingParams,
      searchParams: Booking): Observable<PageResponse<Booking>> {
    console.log('log: get');

    let urlParams = new HttpParams();
    urlParams = getPaginationUrlParams(urlParams, paginationParams);
    urlParams = getSortingUrlParams(urlParams, sortingParams);
    urlParams = BookingManagementService.getSearchUrlParams(urlParams, searchParams);
    console.log(`url params: ${urlParams}`);
    return this.http.get<PageResponse<Booking>>(this.bookingReceptionistApi, {params: urlParams});
  }

  confirm(bookingId: string): Observable<void> {
    return this.http.patch<void>(`${this.bookingReceptionistApi}/${bookingId}/confirm`, null);
  }

  finish(bookingId: string): Observable<void> {
    return this.http.patch<void>(`${this.bookingReceptionistApi}/${bookingId}/finish`, null);
  }

  reject(bookingId: string): Observable<void> {
    return this.http.patch<void>(`${this.bookingReceptionistApi}/${bookingId}/reject`, null);
  }

  private static getSearchUrlParams(params: HttpParams, searchParams: Booking): HttpParams {
    console.log(`search params: ${JSON.stringify(searchParams)}`);
    if (searchParams) {
      if (searchParams.state) {
        params = params.set('state', searchParams.state)
      }

      if (searchParams.customer) {
        if (searchParams.customer.username) {
          params = params.set('customer.username', searchParams.customer.username);
        }

        if (searchParams.customer.email) {
          params = params.set('customer.email', searchParams.customer.email);
        }
      }
    }

    return params;
  }
}
