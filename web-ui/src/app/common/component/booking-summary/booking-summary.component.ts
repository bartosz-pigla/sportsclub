import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SportObject, SportObjectService} from "../../http-service/sport-object.service";
import {SportObjectPosition} from "../../http-service/sport-object-position-service";
import {OpeningTime} from "../../http-service/opening-time-service";
import {BookingSummaryService, SessionBookingDetail} from "../../booking-summary.service";
import {HttpErrorResponse} from "@angular/common/http";
import {MatDialog} from "@angular/material";
import {ErrorHandlerService} from "../../error-handler.service";
import {CustomerBookingService} from "../../http-service/customer-booking.service";
import {BookingSuccessDialog} from "../../dialog/booking-success/booking-success.dialog";
import {forkJoin} from "rxjs";
import {createFromSessionDate} from "../../date-time.utils";
import {BookingDetail} from "../../http-service/booking.model";

@Component({
  selector: 'booking-summary',
  templateUrl: './booking-summary.component.html',
  styleUrls: ['./booking-summary.component.scss'],
  providers: [SportObjectService, ErrorHandlerService, BookingSummaryService, CustomerBookingService]
})
export class BookingSummaryComponent implements OnInit {

  @Input() sportObjectPositions: SportObjectPosition[];
  @Input() openingTimes: OpeningTime[];

  @Output() deleted = new EventEmitter<SessionBookingDetail>();
  @Output() submitted = new EventEmitter();

  sportObjects: SportObject[];
  bookingDetails: SessionBookingDetail[];

  private readonly handleError = (error: HttpErrorResponse) => this.errorHandlerService.showDialog(this.dialog, error);

  constructor(private bookingSummaryService: BookingSummaryService,
              private bookingService: CustomerBookingService,
              private sportObjectService: SportObjectService,
              private dialog: MatDialog,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.sportObjectService.get(
      (sportObjects) => this.sportObjects = sportObjects,
      (error) => this.handleError(error));
    this.initBookingDetails();
  }

  initBookingDetails() {
    this.bookingDetails = this.bookingSummaryService.getDetails();
  }

  getOpeningTime(id: string): OpeningTime {
    return this.openingTimes.find(o => o.id === id);
  }

  getSportObjectPosition(id: string): SportObjectPosition {
    return this.sportObjectPositions.find(p => p.id === id);
  }

  getSportObject(id: string): SportObject {
    return this.sportObjects.find(o => o.id === id);
  }

  delete(detail: SessionBookingDetail) {
    this.bookingSummaryService.delete(detail);
    this.bookingDetails = this.bookingSummaryService.getDetails();
    this.deleted.emit(detail);
  }

  refresh() {
    this.initBookingDetails();
  }

  sumPrices() {
    return this.bookingDetails.map(b => this.getOpeningTime(b.openingTimeId).price).reduce((a, b) => a + b, 0);
  }

  confirm() {
    this.bookingService.create().subscribe(
      (booking) => {
        forkJoin(this.bookingDetails.map(sessionDetail => {
          const detail = new BookingDetail('', sessionDetail.sportObjectPositionId, sessionDetail.openingTimeId, createFromSessionDate(sessionDetail.date));
          return this.bookingService.addDetail(booking.id, detail);
        }))
          .subscribe(
            () => this.bookingService.submit(booking.id)
              .subscribe(
                () => {
                  this.bookingSummaryService.deleteAll();
                  this.dialog.open(BookingSuccessDialog);
                  this.submitted.emit();
                },
                this.handleError),
            this.handleError);
      },
      this.handleError);
  }
}
