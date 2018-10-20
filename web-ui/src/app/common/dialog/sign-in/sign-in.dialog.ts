import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material";
import {Router} from "@angular/router";

@Component({
  selector: 'sign-in-dialog',
  templateUrl: './sign-in.dialog.html',
  styleUrls: ['../dialog.scss']
})
export class SignInDialog {

  constructor(public dialogRef: MatDialogRef<SignInDialog>,
              public router: Router) {
  }

  redirectToSignInPage() {
    this.dialogRef.close(false);
    this.router.navigateByUrl('/public/sign-in');
  }

  redirectToSignUpPage() {
    this.dialogRef.close(false);
    this.router.navigateByUrl('/public/sign-up');
  }
}
