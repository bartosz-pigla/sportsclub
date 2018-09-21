import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Address, Sportsclub} from "../../http-service/sportsclub.service";
import {SportObject} from "../../http-service/sport-object.service";
import {OpeningTime, OpeningTimeService, Time} from "../../http-service/opening-time-service";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorHandlerService} from "../../error-handler.service";
import {MatDialog} from "@angular/material";
import {SportObjectPosition} from "../../http-service/sport-object-position-service";
import {uniquePositionNameValidator} from "./unique-position-name-validator";
import {WeekDay} from "@angular/common";
import {generateOpeningTimesForDay} from "../../opening-time-generator";

@Component({
  selector: 'sport-object-creator',
  templateUrl: './sport-object-creator.component.html',
  styleUrls: ['./sport-object-creator.component.scss'],
  providers: [OpeningTimeService]
})
export class SportObjectCreatorComponent implements OnInit {

  @Input() readonly initialSportObject: SportObject;
  @Input() readonly initialSportObjectPositions: SportObjectPosition[];
  @Input() readonly initialOpeningTimes: OpeningTime[];
  @Input() readonly sportsclub: Sportsclub;
  @Output() submitted: EventEmitter<SportObject> = new EventEmitter<SportObject>();
  @Output() canceled = new EventEmitter();
  basicDataForm: FormGroup;
  address: Address;
  positionForm: FormGroup;
  openingTimeForm: FormGroup;
  positions: SportObjectPosition[] = [];
  private positionsToDelete: string[] = [];
  openingTimes: OpeningTime[] = [];
  private openingTimesToDelete: string[] = [];

  get maxContentLength() {
    return 3000;
  }

  get maxPositionsCount() {
    return 10;
  }

  get mapZoom() {
    return 8;
  }

  get daysOfWeek() {
    return Object.keys(WeekDay).filter(
      (type) => isNaN(<any>type) && type !== 'values'
    );
  }

  private readonly handleError = (error: HttpErrorResponse) => {
    this.errorHandlerService.showDialog(this.dialog, error);
  };

  constructor(private _formBuilder: FormBuilder,
              private openingTimeService: OpeningTimeService,
              private errorHandlerService: ErrorHandlerService,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.initBasicDataForm();
    this.address = this.sportsclub.address;
    this.initPositionForm();

    if(this.initialSportObjectPositions) {
      this.positions = this.initialSportObjectPositions;
    }

    if(this.initialOpeningTimes) {
      this.openingTimes = this.initialOpeningTimes;
    }

    this.initOpeningTimeForm();
  }

  initBasicDataForm() {
    this.basicDataForm = this._formBuilder.group({
      name: [
        this.initialSportObject ? this.initialSportObject.name : '',
        Validators.required
      ],
      description: [
        this.initialSportObject ? this.initialSportObject.description : '',
        [Validators.required, Validators.maxLength(this.maxContentLength)]
      ],
      url: [
        this.initialSportObject ? this.initialSportObject.description : '',
        Validators.required
      ]
    });
  }

  initPositionForm() {
    this.positionForm = this._formBuilder.group({
      name: ['', Validators.required, uniquePositionNameValidator(this.positions)],
      description: ['', Validators.required],
      positionsCount: [1, [Validators.required, Validators.min(1), Validators.max(this.maxPositionsCount)]]
    });
  }

  addPosition() {
    this.positions.push(this.positionForm.value);
    this.resetPositionForm();
  }

  deletePosition(position) {
    const positionIdxToDelete = this.positions.indexOf(position);
    this.positions.splice(positionIdxToDelete, 1);
    if(position.id) {
      this.positionsToDelete.push(position.id);
    }
  }

  resetPositionForm() {
    const lastPositionsCount = this.positionForm.controls.positionsCount.value;
    this.positionForm.reset();
    this.positionForm.controls.positionsCount.setValue(lastPositionsCount);
  }

  initOpeningTimeForm() {
    this.openingTimeForm = this._formBuilder.group({
      dayOfWeek: ['', Validators.required],
      startTime: ['', Validators.required],
      finishTime: ['', Validators.required],
      timeInterval: ['', Validators.required],
      price: [10, [Validators.required, Validators.min(1)]]
    });
  }

  generateOpeningTimesForDay() {
    console.log(`startTimeValue: ${JSON.stringify(Time.createFromString(this.openingTimeForm.controls.startTime.value))}`);
    console.log(`startTimehour: ${Time.createFromString(this.openingTimeForm.controls.startTime.value).hour}`);
    console.log(`startTimeminute: ${Time.createFromString(this.openingTimeForm.controls.startTime.value).minute}`);

    console.log(`intervalTimehour: ${Time.createFromMinutes(this.openingTimeForm.controls.timeInterval.value).hour}`);
    console.log(`price: ${this.openingTimeForm.controls.price.value}`);

    const dayOfWeek = this.openingTimeForm.controls.dayOfWeek.value;

    const dayOpeningTimes = generateOpeningTimesForDay(
      dayOfWeek,
      Time.createFromString(this.openingTimeForm.controls.startTime.value),
      Time.createFromString(this.openingTimeForm.controls.finishTime.value),
      Time.createFromMinutes(this.openingTimeForm.controls.timeInterval.value),
      this.openingTimeForm.controls.price.value);

    console.log(`opening times: ${JSON.stringify(dayOpeningTimes)}`);

    this.deleteOpeningTimes(dayOfWeek);
    this.openingTimes = this.openingTimes.concat(dayOpeningTimes);
  }

  deleteOpeningTimes(dayOfWeek: WeekDay) {
    this.openingTimes
      .filter(openingTime => openingTime.dayOfWeek === dayOfWeek)
      .forEach((openingTime, idx, array) => {
        if (openingTime.id) {
          this.openingTimesToDelete.push(openingTime.id);
        }
        array.splice(idx, 1);
      });
  }

  // initOpeningTimes() {
  //   if (this.initialSportObject) {
  //     this.openingTimeService.get(this.initialSportObject.id).subscribe(
  //       (openingTimes: OpeningTime[]) => {
  //         this.openingTimes = openingTimes;
  //       },
  //       (error: HttpErrorResponse) => {
  //         this.handleError(error);
  //       }
  //     )
  //   }
  // }

  updateAddressForm(newMarkerData) {
    this.address.latitude = newMarkerData.coords.lat;
    this.address.longitude = newMarkerData.coords.lng;
    console.log(`updated address: ${JSON.stringify(this.address)}`);
  }
}
