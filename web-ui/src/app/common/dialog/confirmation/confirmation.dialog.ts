import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material";

@Component({
  selector: 'confirmation-dialog',
  templateUrl: './confirmation.dialog.html',
  styleUrls: ['../dialog.scss']
})
export class ConfirmationDialog {


  constructor(public dialogRef: MatDialogRef<ConfirmationDialog>) {
  }
}
