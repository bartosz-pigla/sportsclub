import {Observable} from "rxjs";
import {PageResponse} from "./page-response";

export class PaginationParams {

  constructor(public page: number,
              public size: number) {
  }
}

export class SortingParams {

  constructor(public sortBy: string,
              public sortOrder: SortOrder) {
  }
}

export enum SortOrder {

  ASC = "ASC",
  DESC = "DESC"
}

export interface IPageableAndSortableGetService<T> {

  get(paginationParams: PaginationParams, sortingParams: SortingParams): Observable<PageResponse<T>>;
}

export interface IDeletableService<T> {

  delete(id: string): Observable<void>;
}

export function getUrlParams(paginationParams: PaginationParams, sortingParams: SortingParams) {
  return `?page=${paginationParams.page}&size=${paginationParams.size}&sort=${sortingParams.sortBy},${sortingParams.sortOrder}`;
}
