import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material";

@Component({
  selector: 'booking-success-dialog',
  templateUrl: './booking-success.dialog.html'
})
export class BookingSuccessDialog {

  constructor(public dialogRef: MatDialogRef<BookingSuccessDialog>) {
  }
}
