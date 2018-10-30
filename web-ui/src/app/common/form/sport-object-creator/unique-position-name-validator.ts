import {SportObjectPosition} from "../../http-service/sport-object-position-service";
import {AbstractControl, ValidatorFn} from "@angular/forms";
import {Observable} from "rxjs";
import { of } from 'rxjs';
import {map} from "rxjs/operators";

export function uniquePositionNameValidator(positions: SportObjectPosition[]): ValidatorFn {
  return (control: AbstractControl) => {
    return of(control.value !== undefined && positions.find(p => p.name === control.value))
      .pipe(map(result => result ? { uniquePositionName: true } : null));
  };
}
