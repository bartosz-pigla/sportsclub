import {Injectable} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root',
})
export class BackendErrorService {

  private static readonly backendErrorPrefix = 'backendError';

  getErrorCodes(errorResponse: HttpErrorResponse): string[] {
    return errorResponse.error
      .map(error => `${BackendErrorService.backendErrorPrefix}.${error.field}.${error.code}`);
  }
}
