import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material";

@Component({
  selector: 'app-connection-error-dialog',
  templateUrl: './connection-error-dialog.component.html',
  styleUrls: ['./connection-error-dialog.component.scss']
})
export class ConnectionErrorDialog {

  constructor(public dialogRef: MatDialogRef<ConnectionErrorDialog>) {
  }

  close() {
    this.dialogRef.close();
  }
}
