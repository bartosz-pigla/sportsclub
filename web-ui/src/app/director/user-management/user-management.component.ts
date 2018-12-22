import {Component, OnInit, ViewChild} from '@angular/core';
import {SortField} from "../../common/component/list-view/list-view.model";
import {SortingParams, SortOrder} from "../../common/http-service/http-service.service";
import {ListViewComponent} from "../../common/component/list-view/list-view.component";
import {TranslateService} from "@ngx-translate/core";
import {User, UserService} from "../../common/http-service/user.service";
import {ConfirmationDialog} from "../../common/dialog/confirmation/confirmation.dialog";
import {MatDialog} from "@angular/material";
import {ErrorDialog} from "../../common/dialog/error/error.dialog";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss'],
  providers: [UserService]
})
export class UserManagementComponent implements OnInit {

  pageSize: number;
  users: User[];
  userFormIsVisible: boolean;
  sortFields: SortField[];
  defaultSort: SortingParams;
  searchForm: FormGroup;

  @ViewChild(ListViewComponent)
  private paginationComponent: ListViewComponent<User>;

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    public userService: UserService,
    private translate: TranslateService) {
  }

  ngOnInit() {
    this.initSortFields();
    this.initSearchForm();
    this.pageSize = 5;
    this.defaultSort = new SortingParams('username', SortOrder.ASC);
  }

  initSearchForm() {
    this.searchForm = this.formBuilder.group({
      username: [''],
      password: [''],
      userType: [''],
      email: ['', Validators.email],
      phoneNumber: ['']
    });
  }

  initSortFields() {
    this.translate.get('listView.sorting.user').subscribe((res: any) => {
      this.sortFields = [
        {name: 'username', viewValue: res['username']},
        {name: 'email', viewValue: res['email']},
        {name: 'userType', viewValue: res['userType']},
      ];
    });
  }

  showUserForm() {
    this.userFormIsVisible = true;
    window.scrollTo(0, 0);
  }

  hideUserForm() {
    this.userFormIsVisible = false;
    this.refreshPage();
  }

  refreshPage() {
    this.paginationComponent.refreshPage();
  }

  initUsers(users) {
    this.users = users;
  }

  showChangeActivationConfirmDialog(userId: string, activated: boolean) {
    const dialogRef = this.dialog.open(ConfirmationDialog);

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.activate(userId, activated);
      }
    });
  }

  activate(userId: string, activated: boolean) {
    this.userService.activate(userId, activated).subscribe(
      () => this.refreshPage(),
      (error: HttpErrorResponse) => {
        this.dialog.open(ErrorDialog, {
          data: error
        });
      }
    );
  }

  filterUsers() {
    this.paginationComponent.setSearchParams(this.searchForm.value);
  }

  clearFilter() {
    this.searchForm.reset();
    this.paginationComponent.setSearchParams(this.searchForm.value);
  }
}
