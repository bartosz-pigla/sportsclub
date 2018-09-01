import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IDeletableService} from "../../http-service/http-service.service";
import {MatDialog} from "@angular/material";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorHandlerService} from "../../error-handler.service";
import {ConfirmationDialog} from "../../dialog/confirmation/confirmation.dialog";

@Component({
  selector: 'delete-item',
  templateUrl: './delete-item.component.html',
  styleUrls: ['./delete-item.component.scss']
})
export class DeleteItemComponent<T> {

  @Input() private readonly service: IDeletableService<T>;
  @Input() private readonly id: string;
  @Output() deleted = new EventEmitter();

  private readonly handleError = (error: HttpErrorResponse) => {
    this.errorHandlerService.showDialog(this.dialog, error);
  };

  constructor(private dialog: MatDialog,
              private errorHandlerService: ErrorHandlerService) {
  }

  showDeleteConfirmDialog() {
    const dialogRef = this.dialog.open(ConfirmationDialog);
    dialogRef.afterClosed().subscribe(confirmed => {
      if(confirmed) {
        this.delete();
      }
    })
  }

  delete() {
    this.service.delete(this.id).subscribe(
      () => {
        this.deleted.emit();
      },
      (error) => {
        this.handleError(error);
      }
    );
  }
}
