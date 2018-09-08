import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {BackendErrorService} from "../../backend-error.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'error-dialog',
  templateUrl: './error.dialog.html',
  styleUrls: ['../dialog.scss']
})
export class ErrorDialog implements OnInit{

  errorCodes: string[];

  constructor(public dialogRef: MatDialogRef<ErrorDialog>,
              private backendErrorService: BackendErrorService,
              @Inject(MAT_DIALOG_DATA) public errorResponse: HttpErrorResponse) {
  }

  ngOnInit(): void {
    this.errorCodes = this.backendErrorService.getErrorCodes(this.errorResponse);
  }
}
