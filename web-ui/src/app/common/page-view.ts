import {PageResponse} from "./page-response";
import {Observable} from "rxjs";

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

  hasPrevious() {
    return this.currentPage > 1;
  }

  hasNext() {
    return this.currentPage < this.totalPages;
  }

  previous(pageResponse: Observable<PageResponse<T>>, fail: () => void) {
    pageResponse.subscribe(
      (response) => {
        this.currentPage--;
        this.updatePageView(response);
      },
      () => {
        fail();
      }
    )
  }

  next(pageResponse: Observable<PageResponse<T>>, fail: () => void) {
    pageResponse.subscribe(
      (response) => {
        this.currentPage++;
        this.updatePageView(response);
      },
      () => {
        fail();
      }
    )
  }

  updatePageView(response: PageResponse<T>) {
    this.totalElements = response.totalElements;
    this.totalPages = response.totalElements / this.pageSize;
    this.content = response.content;
  }
}
