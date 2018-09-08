import {Injectable} from "@angular/core";
import {MatDialog} from "@angular/material";
import {TranslateService} from "@ngx-translate/core";
import {HttpErrorResponse} from "@angular/common/http";
import {UndefinedErrorDialog} from "./dialog/undefined-error/undefined-error.dialog";

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
