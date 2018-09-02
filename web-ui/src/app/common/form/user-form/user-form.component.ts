import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";
import {MatDialog} from "@angular/material";
import {ErrorHandlerService} from "../../error-handler.service";
import {User, UserService, UserType} from "../../http-service/user.service";
import {Observable} from "rxjs";
import {BackendErrors} from "../backend-errors/backend-errors.model";

@Component({
  selector: 'user-form',
  templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {

  @Output() userSubmitted: EventEmitter<User> = new EventEmitter<User>();
  @Output() canceled = new EventEmitter();
  userForm: FormGroup;
  backendErrors: any[];

  private readonly handleError = (error: HttpErrorResponse) => {
    this.errorHandlerService.showDialog(this.dialog, error);
  };

  constructor(
    public userService: UserService,
    private translate: TranslateService,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private errorHandlerService: ErrorHandlerService
  ) {
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
      phoneNumber: ['', Validators.required]
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
      (user) => {
        this.userSubmitted.emit(user);
      },
      (error: HttpErrorResponse) => {
        this.backendErrors = error.error;
      }
    );
  }

  cancel() {
    this.canceled.emit();
  }

  isInvalid(): boolean {
    //return Object.keys(this.userForm.controls).find(key => Object.keys(this.userForm.get(key).errors).find(keyError => keyError != 'backendError') !== undefined) !== undefined;
    //return Object.keys(this.userForm.controls).find(key => true) !== undefined;



    return Object.keys(this.userForm.controls).(key => {
      const controlErrors: ValidationErrors = this.userForm.get(key).errors;

      if(controlErrors) {
        return Object.keys(controlErrors).find(keyError => true);
      } else {
        return false;
      }
    });


    // Object.keys(this.userForm.controls).forEach(key => {
    //
    //   const controlErrors: ValidationErrors = this.userForm.get(key).errors;
    //   if (controlErrors != null) {
    //     Object.keys(controlErrors).forEach(keyError => {
    //       console.log('Key control: ' + key + ', keyError: ' + keyError + ', err value: ', controlErrors[keyError]);
    //     });
    //   }
    // });
  }
}

