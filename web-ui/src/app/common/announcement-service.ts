import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {PageResponse} from "./page-response";
import {Observable} from "rxjs";

export class Announcement {
  constructor(public id: string,
              public title: string,
              public content: string,
              public lastModificationDate: Date) {
  }
}

@Injectable()
export class AnnouncementService {

  readonly pageSize: number = 10;

  constructor(private http: HttpClient) {
  }

  // get(
  //   success: (announcements: PageResponse<Announcement>) => void,
  //   fail: () => void,
  //   page: number = 1,
  //   size: number = this.pageSize,
  //   sortBy: string = 'lastModificationDate',
  //   sortOrder: string = 'DESC') {
  //
  //   let url = `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/announcement`;
  //
  //   this.http.get<PageResponse<Announcement>>(url).subscribe(
  //     (data: PageResponse<Announcement>) => {
  //       success(data);
  //     },
  //     () => {
  //       fail();
  //     });
  // }

  get(
    page: number = 1,
    size: number = this.pageSize,
    sortBy: string = 'lastModificationDate',
    sortOrder: string = 'DESC'): Observable<PageResponse<Announcement>> {

    let url = `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/announcement`;
    return this.http.get<PageResponse<Announcement>>(url);
  }
}
