import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {BookingDetailWithOpeningTimeAndPosition, BookingService} from "../../common/http-service/booking-service";
import {MatDialog} from "@angular/material";
import {ErrorHandlerService} from "../../common/error-handler.service";
import {HttpErrorResponse} from "@angular/common/http";
import {SportObject, SportObjectService} from "../../common/http-service/sport-object.service";
import {addDayToDate, days, OpeningTime, OpeningTimeService} from "../../common/http-service/opening-time-service";
import {SportObjectPosition, SportObjectPositionService} from "../../common/http-service/sport-object-position-service";

@Component({
  selector: 'app-sport-object',
  templateUrl: './sport-object.component.html',
  styleUrls: ['./sport-object.component.scss'],
  providers: [BookingService, SportObjectService, SportObjectPositionService, OpeningTimeService]
})
export class SportObjectComponent implements OnInit {

  sportObject: SportObject;
  sportObjectPositions: SportObjectPosition[];
  bookingDetails: BookingDetailWithOpeningTimeAndPosition[];
  openingTimes: OpeningTime[];

  date: Date;
  previousDate: Date;
  nextDate: Date;

  private readonly handleError = (error: HttpErrorResponse) => this.errorHandlerService.showDialog(this.dialog, error);

  constructor(private route: ActivatedRoute,
              private bookingService: BookingService,
              private sportObjectService: SportObjectService,
              private sportObjectPositionService: SportObjectPositionService,
              private openingTimeService: OpeningTimeService,
              private dialog: MatDialog,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.date = new Date();
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
    this.bookingService.get(objectId, this.date).subscribe(
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

  getBookingForOpeningTime(openingTimeId) {
    return this.bookingDetails.filter(b => b.openingTimeId === openingTimeId);
  }

  getNameOfDayFromDate(date: Date) {
    const nameOfDay = days[date.getDay()];
    return nameOfDay.charAt(0) + nameOfDay.substr(1).toLowerCase();
  }

  getFreePositions(detail: BookingDetailWithOpeningTimeAndPosition) {
    return detail.positionsCount - detail.bookedPositionsCount;
  }
}
