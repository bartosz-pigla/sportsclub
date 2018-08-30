import {IPageableAndSortableGetService, PaginationParams, SortingParams} from "./http-service/http-service.service";
import {Observable} from "rxjs";
import {PageResponse} from "./http-service/page-response";

export class PageView<T> {
  constructor(public currentPage: number,
              public pageSize: number,
              public totalPages: number,
              public totalElements: number,
              public content: T[]) {
  }

  static of<T>(pageSize: number, response: PageResponse<T>): PageView<T> {
    return new PageView<T>(
      1,
      pageSize,
      response.totalElements / pageSize,
      response.totalElements,
      response.content);
  }

  static createEmpty<T>(pageSize: number): PageView<T> {
    return new PageView<T>(0, pageSize, Number.MAX_VALUE, Number.MAX_VALUE, []);
  }

  hasPrevious() {
    return this.currentPage > 1;
  }

  hasNext() {
    return this.currentPage < this.totalPages;
  }

  previous(service: IPageableAndSortableGetService<T>, error: () => void) {
    this.previousHelper(service.get(this.getPreviousPaginationParams()), error);
  }

  previousSorted(service: IPageableAndSortableGetService<T>, sortingParams: SortingParams, error: () => void) {
    this.previousHelper(service.getSorted(this.getPreviousPaginationParams(), sortingParams), error);
  }

  private getPreviousPaginationParams(): PaginationParams {
    return new PaginationParams(this.currentPage + 1, this.pageSize);
  }

  private previousHelper(getResponse: Observable<PageResponse<T>>, error: () => void) {
    if (this.hasPrevious()) {
      getResponse.subscribe(
        (response) => {
          this.currentPage--;
          this.updatePageView(response);
        },
        () => {
          error();
        }
      );
    }
  }

  next(service: IPageableAndSortableGetService<T>, error: () => void) {
    this.nextHelper(service.get(this.getNextPaginationParams()), error);
  }

  nextSorted(service: IPageableAndSortableGetService<T>, sortingParams: SortingParams, error: () => void) {
    this.nextHelper(service.getSorted(this.getNextPaginationParams(), sortingParams), error);
  }

  private getNextPaginationParams(): PaginationParams {
    return new PaginationParams(this.currentPage + 1, this.pageSize);
  }

  private nextHelper(getResponse: Observable<PageResponse<T>>, error: () => void) {
    console.log('next helper');
    console.log(this);
    if (this.hasNext()) {
      console.log('has next');
      getResponse.subscribe(
        (response) => {
          console.log(response);
          this.currentPage++;
          this.updatePageView(response);
        },
        () => {
          error();
        }
      );
    }
  }

  updatePageView(response: PageResponse<T>) {
    this.totalElements = response.totalElements;
    this.totalPages = response.totalElements / this.pageSize;
    this.content = response.content;
  }
}
