import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {PageResponse} from "./page-response";
import {Observable} from "rxjs";
import {getPaginationUrlParams, getSortingUrlParams, IPageableAndSortableGetService, PaginationParams, SortingParams} from "./http-service.service";

export class Announcement {
  constructor(public id: string,
              public title: string,
              public content: string,
              public lastModificationDate: Date) {
  }
}

@Injectable()
export class AnnouncementService implements IPageableAndSortableGetService<Announcement> {

  private readonly getBaseUrl: string = `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}`;

  constructor(private http: HttpClient) {
  }

  get(paginationParams: PaginationParams): Observable<PageResponse<Announcement>> {
    return this.http.get<PageResponse<Announcement>>(
      this.getBaseUrl + getPaginationUrlParams(paginationParams));
  }

  getSorted(paginationParams: PaginationParams, sortingParams: SortingParams): Observable<PageResponse<Announcement>> {
    return this.http.get<PageResponse<Announcement>>(
      this.getBaseUrl + getPaginationUrlParams(paginationParams) + getSortingUrlParams(sortingParams));
  }
}
