import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SportObject, SportObjectService} from "../../http-service/sport-object.service";
import {SportObjectPosition} from "../../http-service/sport-object-position-service";
import {OpeningTime} from "../../http-service/opening-time-service";
import {BookingDetail} from "../../http-service/booking-service";
import {BookingSummaryService, SessionBookingDetail} from "../../booking-summary.service";
import {HttpErrorResponse} from "@angular/common/http";
import {MatDialog} from "@angular/material";
import {ErrorHandlerService} from "../../error-handler.service";

@Component({
  selector: 'booking-summary',
  templateUrl: './booking-summary.component.html',
  styleUrls: ['./booking-summary.component.scss'],
  providers: [SportObjectService, ErrorHandlerService, BookingSummaryService]
})
export class BookingSummaryComponent implements OnInit {

  @Input() sportObjectPositions: SportObjectPosition[];
  @Input() openingTimes: OpeningTime[];

  @Output() deleted = new EventEmitter<BookingDetail>();

  sportObjects: SportObject[];
  bookingDetails: SessionBookingDetail[];

  private readonly handleError = (error: HttpErrorResponse) => this.errorHandlerService.showDialog(this.dialog, error);

  constructor(private bookingSummaryService: BookingSummaryService,
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

  delete(detail: BookingDetail) {
    this.bookingDetails = this.bookingDetails.filter(b => b.bookingDetail !== detail);
    this.bookingSummaryService.delete(detail);
    this.deleted.emit(detail);
  }

  refresh() {
    this.initBookingDetails();
  }

  sumPrices() {
    return this.bookingDetails.map(b => this.getOpeningTime(b.bookingDetail.openingTimeId).price).reduce((a, b) => a + b, 0);
  }
}
