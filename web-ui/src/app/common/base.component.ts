import {MatDialog} from "@angular/material";
import {ConnectionErrorDialog} from "./component/connection-error-dialog/connection-error-dialog.component";

export class BaseComponent {

  protected readonly openDefaultErrorDialog = () => {
    this.openConnectionErrorDialog();
  };

  constructor(private dialog: MatDialog
  ) {
  }

  protected openConnectionErrorDialog(): void {
    this.dialog.open(ConnectionErrorDialog);
  }
}

