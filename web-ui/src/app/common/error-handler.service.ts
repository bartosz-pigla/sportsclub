import {Injectable} from "@angular/core";
import {MatDialog} from "@angular/material";
import {TranslateService} from "@ngx-translate/core";
import {HttpErrorResponse} from "@angular/common/http";
import {UndefinedErrorDialog} from "./dialog/undefined-error/undefined-error.dialog";

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerService {

  constructor(private translate: TranslateService) {
  }

  showDialog(dialog: MatDialog, error: HttpErrorResponse) {
    switch (error.status) {
      case 404:
        this.handleBadRequestError(error);
        break;
      default:
        dialog.open(UndefinedErrorDialog);
    }
  }

  private handleBadRequestError(error: HttpErrorResponse) {
  }
}
