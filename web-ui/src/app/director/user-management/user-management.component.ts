import {Component, OnInit, ViewChild} from '@angular/core';
import {Announcement, AnnouncementService} from "../../common/http-service/announcement-service";
import {SortField} from "../../common/component/list-view/list-view.model";
import {SortingParams, SortOrder} from "../../common/http-service/http-service.service";
import {ListViewComponent} from "../../common/component/list-view/list-view.component";
import {TranslateService} from "@ngx-translate/core";
import {User, UserService} from "../../common/http-service/user.service";

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
  searchParams = new User();

  @ViewChild(ListViewComponent)
  private paginationComponent: ListViewComponent<User>;

  constructor(
    public userService: UserService,
    private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.initSortFields();
    this.pageSize = 10;
    this.defaultSort = new SortingParams('username', SortOrder.ASC);
  }

  initSortFields() {
    let key = "listView.sorting.user";

    this.translate.get([key]).subscribe((res: any) => {
      let value = res[key];
      this.sortFields = [
        {name: 'username', viewValue: value['username']},
        {name: 'email', viewValue: value['email']},
        {name: 'userType', viewValue: value['userType']},
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
}
