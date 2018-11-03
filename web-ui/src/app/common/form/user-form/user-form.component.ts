import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";
import {User, UserService, UserType} from "../../http-service/user.service";
import {Observable} from "rxjs";
import {CustomValidators} from "../../validator/custom-validators";

@Component({
  selector: 'user-form',
  templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {

  @Output() userSubmitted: EventEmitter<User> = new EventEmitter<User>();
  @Output() canceled = new EventEmitter();
  userForm: FormGroup;
  errorResponse: HttpErrorResponse;

  constructor(
    public userService: UserService,
    private translate: TranslateService,
    private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.initUserForm();
  }

  get controls() {
    return this.userForm.controls;
  }

  initUserForm() {
    this.userForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      userType: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required, CustomValidators.phoneNumber]
    });
  }

  createUser() {
    const user: User = this.userForm.value;
    let userCreatedObservable: Observable<User>;

    switch (user.userType) {
      case UserType.CUSTOMER:
        userCreatedObservable = this.userService.postCustomer(user);
        break;
      case UserType.RECEPTIONIST:
        userCreatedObservable = this.userService.postReceptionist(user);
        break;
      case UserType.DIRECTOR:
        userCreatedObservable = this.userService.postDirector(user);
        break;
    }

    userCreatedObservable.subscribe(
      user => this.userSubmitted.emit(user),
      (error: HttpErrorResponse) => this.errorResponse = error);
  }

  cancel() {
    this.canceled.emit();
  }
}

