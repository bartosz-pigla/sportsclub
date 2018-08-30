import {Observable} from "rxjs";
import {PageResponse} from "./page-response";

export class PaginationParams {

  constructor(public page: number,
              public size: number) {
  }
}

export class SortingParams {

  constructor(public sortBy: string,
              public sortOrder: SortingOrder) {
  }
}

export enum SortingOrder {

  ASC = "ASC",
  DESC = "DESC"
}

export interface IPageableAndSortableGetService<T> {

  get(paginationParams: PaginationParams): Observable<PageResponse<T>>;

  getSorted(paginationParams: PaginationParams, sortingParams?: SortingParams): Observable<PageResponse<T>>;
}

export function getPaginationUrlParams(paginationParams: PaginationParams) {
  return `?page=${paginationParams.page}&size=${paginationParams.size}`;
}

export function getSortingUrlParams(sortingParams: SortingParams) {
  return `&sort=${sortingParams.sortBy},${sortingParams.sortOrder}`;
}
