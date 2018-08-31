import {IPageableAndSortableGetService, PaginationParams, SortingParams, SortOrder} from "../../http-service/http-service.service";
import {Observable} from "rxjs";
import {PageResponse} from "../../http-service/page-response";

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

  static of<T>(pageSize: number, response: PageResponse<T>): ListViewModel<T> {
    return new ListViewModel<T>(0, pageSize, response.totalPages, response.content);
  }

  static createEmpty<T>(pageSize: number): ListViewModel<T> {
    return new ListViewModel<T>(-1, pageSize, Number.MAX_VALUE, []);
  }

  refresh(service: IPageableAndSortableGetService<T>,
          sortingParams: SortingParams,
          success: () => void,
          error: () => void) {
    let paginationParams = new PaginationParams(this.currentPage, this.pageSize);
    this.loadPage(paginationParams.page, service.get(paginationParams, sortingParams), success, error);
  }

  hasPrevious() {
    return this.currentPage > 0;
  }

  hasNext() {
    return this.currentPage < this.totalPages - 1;
  }

  previous(service: IPageableAndSortableGetService<T>,
           sortingParams: SortingParams,
           success: () => void,
           error: () => void) {
    if (this.hasPrevious()) {
      let paginationParams = new PaginationParams(this.currentPage - 1, this.pageSize);
      this.loadPage(paginationParams.page, service.get(paginationParams, sortingParams), success, error);
    }
  }

  next(service: IPageableAndSortableGetService<T>,
       sortingParams: SortingParams,
       success: () => void,
       error: () => void) {
    if (this.hasNext()) {
      let paginationParams = new PaginationParams(this.currentPage + 1, this.pageSize);
      this.loadPage(paginationParams.page, service.get(paginationParams, sortingParams), success, error);
    }
  }

  private loadPage(page: number,
                   getResponse: Observable<PageResponse<T>>,
                   success: () => void,
                   error: () => void) {
    getResponse.subscribe(
      (response) => {
        this.currentPage = page;
        this.updatePageView(response);
        success();
      },
      () => {
        error();
      }
    );
  }

  updatePageView(response: PageResponse<T>) {
    this.totalPages = response.totalPages;
    this.content = response.content;
  }
}
