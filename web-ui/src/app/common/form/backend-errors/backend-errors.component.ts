import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {BackendErrorService} from "../../backend-error.service";

@Component({
  selector: 'backend-errors',
  templateUrl: './backend-errors.component.html',
  styleUrls: ['./backend-errors.component.scss']
})
export class BackendErrorsComponent implements OnChanges {

  @Input() errorResponse: HttpErrorResponse;
  errorCodes: string[];

  constructor(private backendErrorService: BackendErrorService) {
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      let currentResponse = changes[propName].currentValue;

      if (propName === 'errorResponse' && currentResponse) {
        this.errorResponse = currentResponse;
        this.errorCodes = this.backendErrorService.getErrorCodes(this.errorResponse);
      }
    }
  }
}
