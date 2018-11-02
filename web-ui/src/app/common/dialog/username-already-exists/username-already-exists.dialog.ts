import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material";

@Component({
  selector: 'username-already-exists',
  templateUrl: './username-already-exists.dialog.html'
})
export class UsernameAlreadyExistsDialog {

  constructor(public dialogRef: MatDialogRef<UsernameAlreadyExistsDialog>) {
  }

}
