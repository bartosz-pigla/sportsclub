import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {SortField} from "../../common/component/list-view/list-view.model";
import {SortingParams, SortOrder} from "../../common/http-service/http-service.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ListViewComponent} from "../../common/component/list-view/list-view.component";
import {ErrorHandlerService} from "../../common/error-handler.service";
import {MatDialog} from "@angular/material";
import {TranslateService} from "@ngx-translate/core";
import {Booking} from "../../common/http-service/booking.model";
import {BookingManagementService} from "../../common/http-service/booking-management.service";
import {User} from "../../common/http-service/user.service";
import Timer = NodeJS.Timer;

@Component({
  selector: 'app-booking-management',
  templateUrl: './booking-management.component.html',
  styleUrls: ['./booking-management.component.scss'],
  providers: [BookingManagementService]
})
export class BookingManagementComponent implements OnInit, OnDestroy {

  bookings: Booking[];
  sortFields: SortField[];
  defaultSort: SortingParams;
  searchForm: FormGroup;
  pageSize: number;
  private readonly bookingLoadingFrequencySec = 10;

  @ViewChild(ListViewComponent)
  paginationComponent: ListViewComponent<Booking>;
  timer: Timer;

  constructor(public bookingService: BookingManagementService,
              private errorHandlerService: ErrorHandlerService,
              private dialog: MatDialog,
              private translate: TranslateService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.pageSize = 5;
    this.defaultSort = new SortingParams('date', SortOrder.DESC);
    this.initSortFields();
    this.initSearchForm();
    this.timer = setInterval(() => this.paginationComponent.refreshPage(), this.bookingLoadingFrequencySec * 1000);
  }

  ngOnDestroy(): void {
    clearTimeout(this.timer);
  }

  initSortFields() {
    this.translate.get("listView.sorting.booking").subscribe((res: any) => {
      this.sortFields = [
        {name: 'date', viewValue: res['date']},
        {name: 'customer.username', viewValue: res['username']},
        {name: 'customer.email', viewValue: res['email']}
      ];
    });
  }

  initSearchForm() {
    this.searchForm = this.formBuilder.group({
      state: [''],
      date: [new Date()],
      username: [''],
      email: []
    });
  }

  initBookings(bookings) {
    this.bookings = bookings;
  }

  filterBookings() {
    const user = new User(null, this.searchForm.controls.username.value, null, this.searchForm.controls.email.value, null, null, null);
    const booking = new Booking('', this.searchForm.controls.date.value, this.searchForm.controls.state.value, user);
    this.paginationComponent.setSearchParams(booking);
  }

  clearFilter() {
    this.searchForm.reset();
    this.paginationComponent.setSearchParams(null);
  }
}
