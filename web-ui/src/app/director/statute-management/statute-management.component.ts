import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StatuteService} from "../../common/http-service/statute-service";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorHandlerService} from "../../common/error-handler.service";
import {MatDialog} from "@angular/material";
import {ActivatedRoute} from "@angular/router";
import {PreviousPageService} from "../../common/navigation/previous-page.service";

@Component({
  selector: 'app-statute-management',
  templateUrl: './statute-management.component.html',
  styleUrls: ['./statute-management.component.scss'],
  providers: [StatuteService]
})
export class StatuteManagementComponent implements OnInit {

  statuteForm: FormGroup;

  private errorHandler = (error: HttpErrorResponse) => this.errorHandlerService.showDialog(this.dialog, error);

  constructor(private statuteService: StatuteService,
              private formBuilder: FormBuilder,
              private errorHandlerService: ErrorHandlerService,
              private dialog: MatDialog,
              private activatedRoute: ActivatedRoute,
              private previousPageService: PreviousPageService) {
  }

  ngOnInit() {
    this.statuteForm = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
    });

    this.statuteService.get().subscribe(
      statute => {
        this.statuteForm.setValue({
          title: statute.title,
          description: statute.description
        });
      },
      error => this.errorHandler(error));
  }

  updateStatute() {
    this.statuteService.post(this.statuteForm.value).subscribe(
      () => {
        this.previousPageService.navigate(this.activatedRoute);
      },
      (error) => this.errorHandler(error));
  }
}
