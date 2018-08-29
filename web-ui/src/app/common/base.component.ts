import {MatDialog} from "@angular/material";
import {ConnectionErrorDialog} from "./component/connection-error-dialog/connection-error-dialog.component";

export class BaseComponent {

  constructor(private dialog: MatDialog
  ) {
  }

  protected openConnectionErrorDialog(): void {
    this.dialog.open(ConnectionErrorDialog);
  }
}

