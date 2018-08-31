import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {PageResponse} from "./page-response";
import {Observable} from "rxjs";
import {
  getPaginationUrlParams,
  getSortingUrlParams,
  getUrlParams,
  IPageableAndSortableGetService,
  PaginationParams,
  SortingParams
} from "./http-service.service";

export class Announcement {
  constructor(public id: string,
              public title: string,
              public content: string,
              public lastModificationDate: Date) {
  }
}

@Injectable()
export class AnnouncementService implements IPageableAndSortableGetService<Announcement> {

  private readonly announcementPublicApi: string = `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/announcement`;
  private readonly announcementDirectorApi: string = `${environment.apiUrl}/director-api/sportsclub/${environment.sportsclubId}/announcement`;

  constructor(private http: HttpClient) {
  }

  get(paginationParams: PaginationParams, sortingParams: SortingParams): Observable<PageResponse<Announcement>> {
    return this.http.get<PageResponse<Announcement>>(this.announcementPublicApi + getUrlParams(paginationParams, sortingParams));
  }

  post(announcement: Announcement): Observable<Announcement> {
    console.log(this.announcementDirectorApi);
    return this.http.post<Announcement>(this.announcementDirectorApi, announcement);
  }
}
