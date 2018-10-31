import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomerBookingService} from "../../common/http-service/customer-booking.service";
import {ListViewComponent} from "../../common/component/list-view/list-view.component";
import {MatDialog} from "@angular/material";
import {ErrorHandlerService} from "../../common/error-handler.service";
import {TranslateService} from "@ngx-translate/core";
import {SortField} from "../../common/component/list-view/list-view.model";
import {SortingParams, SortOrder} from "../../common/http-service/http-service.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Booking} from "../../common/http-service/booking.model";

@Component({
  selector: 'app-customer-bookings',
  templateUrl: './customer-bookings.component.html',
  styleUrls: ['./customer-bookings.component.scss'],
  providers: [CustomerBookingService]
})
export class CustomerBookingsComponent implements OnInit {

  bookings: Booking[];
  sortFields: SortField[];
  defaultSort: SortingParams;
  searchForm: FormGroup;
  pageSize: number;

  @ViewChild(ListViewComponent)
  paginationComponent: ListViewComponent<Booking>;

  constructor(public bookingService: CustomerBookingService,
              private errorHandlerService: ErrorHandlerService,
              private dialog: MatDialog,
              private translate: TranslateService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.pageSize = 15;
    this.defaultSort = new SortingParams('date', SortOrder.DESC);
    this.initSortFields();
    this.initSearchForm();
  }

  initSortFields() {
    this.translate.get("listView.sorting.booking").subscribe((res: any) => {
      this.sortFields = [
        {name: 'date', viewValue: res['date']}
      ];
    });
  }

  initSearchForm() {
    this.searchForm = this.formBuilder.group({
      state: [''],
    });
  }

  initBookings(bookings) {
    this.bookings = bookings;
  }

  filterBookings() {
    this.paginationComponent.setSearchParams(this.searchForm.value);
  }

  clearFilter() {
    this.searchForm.reset();
    this.paginationComponent.setSearchParams(this.searchForm.value);
  }
}
