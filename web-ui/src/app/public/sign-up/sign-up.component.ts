import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {PreviousPageService} from "../../common/navigation/previous-page.service";
import {MatDialog} from "@angular/material";
import {AuthenticationService} from "../../common/security/authentication.service";
import {CustomValidators} from "../../common/validator/custom-validators";
import {SignUpUserCommand, UserService} from "../../common/http-service/user.service";
import {ActivationLinkSentDialog} from "../../common/dialog/activation-link-sent/activation-link-sent.dialog";
import {HttpErrorResponse} from "@angular/common/http";
import {UsernameAlreadyExistsDialog} from "../../common/dialog/username-already-exists/username-already-exists.dialog";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss'],
  providers: [UserService]
})
export class SignUpComponent implements OnInit {

  signUpForm: FormGroup;
  signUpFailed: boolean;
  readonly siteKey = environment.captchaSiteKey;

  constructor(
    private activatedRoute: ActivatedRoute,
    private previousPageService: PreviousPageService,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private userService: UserService) {

  }

  ngOnInit() {
    this.signUpFailed = false;
    this.initializeForm();
  }

  private initializeForm() {
    this.signUpForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, CustomValidators.phoneNumber]],
      statuteAccepted: [false, Validators.requiredTrue],
      captcha: [null, Validators.required]
    }, {
      validator: CustomValidators.confirmPassword
    });
  }

  get controls() {
    return this.signUpForm.controls;
  }

  goToPreviousPage() {
    this.previousPageService.navigate(this.activatedRoute);
  }

  signUp() {
    const signUpCommand = new SignUpUserCommand(
      this.signUpForm.value.username,
      this.signUpForm.value.email,
      this.signUpForm.value.phoneNumber,
      this.signUpForm.value.password);

    this.userService.signUp(signUpCommand).subscribe(
      () => this.dialog.open(ActivationLinkSentDialog).afterClosed().subscribe(() => this.goToPreviousPage()),
      (errorResponse: HttpErrorResponse) => {
        if (errorResponse.status === 409) {
          this.dialog.open(UsernameAlreadyExistsDialog).afterClosed().subscribe(
            () => this.signUpForm.get('username').setErrors({usernameAlreadyExists: true}));
        }
      }
    );
  }
}
