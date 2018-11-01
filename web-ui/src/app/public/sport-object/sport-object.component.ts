import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CustomerBookingService} from "../../common/http-service/customer-booking.service";
import {MatDialog} from "@angular/material";
import {ErrorHandlerService} from "../../common/error-handler.service";
import {HttpErrorResponse} from "@angular/common/http";
import {SportObject, SportObjectService} from "../../common/http-service/sport-object.service";
import {OpeningTime, OpeningTimeService} from "../../common/http-service/opening-time-service";
import {SportObjectPosition, SportObjectPositionService} from "../../common/http-service/sport-object-position-service";
import {AuthenticationService} from "../../common/security/authentication.service";
import {SignInDialog} from "../../common/dialog/sign-in/sign-in.dialog";
import {BookingSummaryService} from "../../common/booking-summary.service";
import {BookingSummaryComponent} from "../../common/component/booking-summary/booking-summary.component";
import {addDayToDate, currentDate, days} from "../../common/date-time.utils";
import {BookingDetail, BookingDetailWithOpeningTimeAndPosition} from "../../common/http-service/booking.model";

@Component({
  selector: 'sport-object',
  templateUrl: './sport-object.component.html',
  styleUrls: ['./sport-object.component.scss'],
  providers: [CustomerBookingService, SportObjectService, SportObjectPositionService, OpeningTimeService]
})
export class SportObjectComponent implements OnInit {

  sportObject: SportObject;
  sportObjectPositions: SportObjectPosition[];
  bookingDetails: BookingDetailWithOpeningTimeAndPosition[];
  openingTimes: OpeningTime[];
  date: Date;
  previousDate: Date;
  nextDate: Date;
  mapZoom = 8;

  @ViewChild(BookingSummaryComponent)
  private bookingSummaryComponent: BookingSummaryComponent;

  private readonly handleError = (error: HttpErrorResponse) => this.errorHandlerService.showDialog(this.dialog, error);

  constructor(private route: ActivatedRoute,
              private bookingService: CustomerBookingService,
              private sportObjectService: SportObjectService,
              private sportObjectPositionService: SportObjectPositionService,
              private openingTimeService: OpeningTimeService,
              private dialog: MatDialog,
              private errorHandlerService: ErrorHandlerService,
              public authenticationService: AuthenticationService,
              public bookingSummaryService: BookingSummaryService) {
  }

  ngOnInit() {
    this.date = currentDate();
    this.previousDate = addDayToDate(this.date, -1);
    this.nextDate = addDayToDate(this.date, 1);

    this.route.params.subscribe(params => {
      const objectId = params['id'];
      this.initSportObject(objectId);
      this.initOpeningTimes(objectId);
      this.initSportObjectPositions(objectId);
      this.initBookingDetails(objectId);
    });
  }

  initSportObject(objectId) {
    this.sportObjectService.getOne(objectId, sportObject => this.sportObject = sportObject, this.handleError);
  }

  initBookingDetails(objectId) {
    this.bookingService.getBookingDetail(objectId, this.date).subscribe(
      details => this.bookingDetails = details,
      this.handleError);
  }

  initSportObjectPositions(objectId) {
    this.sportObjectPositionService.get(objectId).subscribe(
      (positions) => this.sportObjectPositions = positions,
      this.handleError
    );
  }

  initOpeningTimes(objectId) {
    this.openingTimeService.getOpeningTimes(objectId).subscribe(openingTimes => this.openingTimes = openingTimes, this.handleError);
  }

  getOpeningTimesInCurrentDate() {
    return this.getOpeningTimesForDay(this.date.getDay());
  }

  getOpeningTimesForDay(dayIdx: number) {
    return this.openingTimes.filter(o => o.dayOfWeek.toString() == days[dayIdx]);
  }

  previousDay() {
    this.changeDay(-1);
    this.initBookingDetails(this.sportObject.id);
  }

  nextDay() {
    this.changeDay(1);
    this.initBookingDetails(this.sportObject.id);
  }

  changeDay(day: number) {
    this.previousDate = addDayToDate(this.previousDate, day);
    this.date = addDayToDate(this.date, day);
    this.nextDate = addDayToDate(this.nextDate, day);
  }

  getDetailsForOpeningTime(openingTimeId): BookingDetailWithOpeningTimeAndPosition[] {
    return this.bookingDetails.filter(b => b.openingTimeId === openingTimeId);
  }

  getNameOfDayFromDate(date: Date) {
    const nameOfDay = days[date.getDay()];
    return nameOfDay.charAt(0) + nameOfDay.substr(1).toLowerCase();
  }

  getFreePositions(detail: BookingDetailWithOpeningTimeAndPosition) {
    let freePositions = detail.positionsCount - detail.bookedPositionsCount;
    return this.bookingSummaryService.detailExists(detail, this.sportObject.id, this.date) ? freePositions - 1 : freePositions;
  }

  book(detail: BookingDetailWithOpeningTimeAndPosition) {
    if (this.authenticationService.isSignedIn()) {
      detail.bookedPositionsCount++;
      this.bookingSummaryService.addDetail(BookingDetail.createFrom(detail, this.date), this.sportObject.id);
      this.bookingSummaryComponent.refresh();
    } else {
      this.dialog.open(SignInDialog);
    }
  }

  onDeleteBookedPosition() {
    this.initBookingDetails(this.sportObject.id);
  }

  canBook() {
    return currentDate() <= this.date;
  }

  clickExample() {
    console.log(`address: ${JSON.stringify(this.sportObject.address)}`);
  }
}
