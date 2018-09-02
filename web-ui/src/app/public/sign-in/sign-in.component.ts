import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {AuthenticationService} from "../../common/security/authentication.service";
import {PreviousPageService} from "../../common/navigation/previous-page.service";
import {MatDialog} from "@angular/material";

@Component({
  selector: 'sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent implements OnInit {

  signInForm: FormGroup;
  signInFailed: boolean = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private previousPageService: PreviousPageService,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.initializeForm();
  }

  private initializeForm() {
    this.signInForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get controls() {
    return this.signInForm.controls;
  }

  goToPreviousPage() {
    this.previousPageService.navigate(this.activatedRoute);
  }

  onSubmit() {
    this.authenticationService.signIn(
      this.signInForm.value.username,
      this.signInForm.value.password,
      () => {
        this.goToPreviousPage();
      },
      () => {
        this.signInFailed = true;
      }
    );
  }
}
