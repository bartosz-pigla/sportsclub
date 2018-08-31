export class PageResponse<T> {
  constructor(public totalPages: number,
              public content: T[]) {

  }
}
