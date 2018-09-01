import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material";

@Component({
  selector: 'undefined-error-dialog',
  templateUrl: './undefined-error.dialog.html',
  styleUrls: ['../dialog.scss']
})
export class UndefinedErrorDialog {

  constructor(public dialogRef: MatDialogRef<UndefinedErrorDialog>) {
  }
}
