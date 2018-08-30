export class PageResponse<T> {
  constructor(public totalElements: number,
              public content: T[]) {

  }
}
