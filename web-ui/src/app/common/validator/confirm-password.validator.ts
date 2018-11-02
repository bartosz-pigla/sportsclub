import {AbstractControl} from "@angular/forms";

export class CustomValidator {

  static confirmPassword(control: AbstractControl) {
    let password = control.get('password');
    let confirmPassword = control.get('confirmPassword');
    if (password.value !== confirmPassword.value) {
      confirmPassword.setErrors({passwordsNotMatches: true});
    } else {
      return null
    }
  }
}
