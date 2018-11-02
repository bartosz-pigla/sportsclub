import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material";

@Component({
  selector: 'activation-link-sent',
  templateUrl: './activation-link-sent.dialog.html'
})
export class ActivationLinkSentDialog {

  constructor(public dialogRef: MatDialogRef<ActivationLinkSentDialog>) {
  }

}
