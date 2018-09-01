import {
  IPageableAndSortableGetService,
  PaginationParams,
  SortingParams,
  SortOrder
} from "../../http-service/http-service.service";
import {Observable} from "rxjs";
import {PageResponse} from "../../http-service/page-response";
import {HttpErrorResponse} from "@angular/common/http";

export class SortField {
  constructor(public name: string,
              public viewValue: string) {

  }
}

export class ListViewModel<T> {
  constructor(public paginationParams: PaginationParams,
              public sortingParams: SortingParams,
              public totalPages: number,
              public content: T[]) {
  }

  static createEmpty<T>(pageSize: number, sortingParams: SortingParams): ListViewModel<T> {
    return new ListViewModel<T>(
      new PaginationParams(0, pageSize),
      new SortingParams(sortingParams.sortBy, sortingParams.sortOrder),
      1,
      []);
  }

  refresh(service: IPageableAndSortableGetService<T>,
          success: () => void,
          error: (response: HttpErrorResponse) => void) {
    this.loadPage(this.paginationParams.page, service.get(this.paginationParams, this.sortingParams), success, error);
  }

  hasPrevious() {
    return this.paginationParams.page > 0;
  }

  hasNext() {
    return this.paginationParams.page < this.totalPages - 1;
  }

  previous(service: IPageableAndSortableGetService<T>, success: () => void, error: (response: HttpErrorResponse) => void) {
    if (this.hasPrevious()) {
      this.paginationParams.page--;
      this.loadPage(this.paginationParams.page, service.get(this.paginationParams, this.sortingParams), success, error);
    }
  }

  next(service: IPageableAndSortableGetService<T>, success: () => void, error: (response: HttpErrorResponse) => void) {
    if (this.hasNext()) {
      this.paginationParams.page++;
      this.loadPage(this.paginationParams.page, service.get(this.paginationParams, this.sortingParams), success, error);
    }
  }

  private loadPage(page: number, getResponse: Observable<PageResponse<T>>, success: () => void, error: (response: HttpErrorResponse) => void) {
    getResponse.subscribe(
      (response) => {
        this.paginationParams.page = page;
        this.totalPages = response.totalPages;
        this.content = response.content;
        success();
      },
      (errorResponse) => {
        error(errorResponse);
      }
    );
  }
}
