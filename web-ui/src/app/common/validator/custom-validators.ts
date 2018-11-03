import {AbstractControl} from "@angular/forms";

export class CustomValidators {

  static confirmPassword(control: AbstractControl) {
    let password = control.get('password');
    let confirmPassword = control.get('confirmPassword');
    if (password.value !== confirmPassword.value) {
      confirmPassword.setErrors({passwordsNotMatches: true});
    }
  }

  static phoneNumber(control: AbstractControl) {
    if (control.value.charAt(0) !== '+' || isNaN(control.value.substring(1)) || control.value.length !== 12) {
      return {'phoneNumber': true};
    }
  }

}
