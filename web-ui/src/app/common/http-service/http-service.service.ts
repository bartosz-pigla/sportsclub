import {Observable} from "rxjs";
import {PageResponse} from "./page-response";
import {HttpParams} from "@angular/common/http";

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

  get(paginationParams: PaginationParams, sortingParams: SortingParams, searchParams: T): Observable<PageResponse<T>>;
}

export interface IDeletableService<T> {

  delete(id: string): Observable<void>;
}

export function getPaginationUrlParams(params: HttpParams, paginationParams: PaginationParams) {
  return params
    .set('page', String(paginationParams.page))
    .set('size', String(paginationParams.size));
}

export function getSortingUrlParams(params: HttpParams, sortingParams: SortingParams) {
  return params.set('sort', `${sortingParams.sortBy},${sortingParams.sortOrder}`);
}
