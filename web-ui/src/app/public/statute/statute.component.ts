import {Component, OnInit} from '@angular/core';
import {Statute, StatuteService} from "../../common/http-service/statute-service";
import {HttpErrorResponse} from "@angular/common/http";
import {MatDialog} from "@angular/material";
import {ErrorHandlerService} from "../../common/error-handler.service";

@Component({
  selector: 'app-statute',
  templateUrl: './statute.component.html',
  styleUrls: ['./statute.component.scss'],
  providers: [StatuteService]
})
export class StatuteComponent implements OnInit {

  statute: Statute;

  private readonly handleError = (error: HttpErrorResponse) => this.errorHandlerService.showDialog(this.dialog, error);

  constructor(private statuteService: StatuteService,
              private dialog: MatDialog,
              private errorHandlerService: ErrorHandlerService,) {
  }

  ngOnInit() {
    this.statuteService.get().subscribe((statute) => this.statute = statute, this.handleError);
  }

}
