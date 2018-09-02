import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';

@Component({
  selector: 'backend-errors',
  templateUrl: './backend-errors.component.html'
})
export class BackendErrorsComponent implements OnChanges {

  private static readonly backendErrorPrefix = 'backendError';

  @Input() fieldName: string;
  @Input() errors: any[];
  // @Output() markFieldAsInvalid: EventEmitter<boolean> = new EventEmitter<boolean>();
  fieldErrors: string[];

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      let currentErrors = changes[propName].currentValue;

      if (propName === 'errors' && currentErrors) {
        this.errors = currentErrors;
        this.updateFieldErrors();
      }
    }
  }

  updateFieldErrors() {
    this.fieldErrors = this.errors
      .filter(error => error.field === this.fieldName)
      .map(error => `${BackendErrorsComponent.backendErrorPrefix}.${this.fieldName}.${error.code}`);

    if (this.fieldErrors.length > 0) {
      // this.markFieldAsInvalid.emit(true);
    }
  }
}
