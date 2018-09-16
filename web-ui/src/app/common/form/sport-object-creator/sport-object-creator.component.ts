import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Form, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Address, Sportsclub} from "../../http-service/sportsclub.service";
import {SportObject} from "../../http-service/sport-object.service";
import {OpeningTime, OpeningTimeService} from "../../http-service/opening-time-service";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorHandlerService} from "../../error-handler.service";
import {MatDialog} from "@angular/material";
import {SportObjectPosition} from "../../http-service/sport-object-position-service";
import {uniquePositionNameValidator} from "./unique-position-name-validator";

@Component({
  selector: 'sport-object-creator',
  templateUrl: './sport-object-creator.component.html',
  styleUrls: ['./sport-object-creator.component.scss'],
  providers: [OpeningTimeService]
})
export class SportObjectCreatorComponent implements OnInit {

  @Input() readonly initialSportObject: SportObject;
  @Input() readonly initialSportObjectPositions: SportObjectPosition[];
  @Input() readonly sportsclub: Sportsclub;
  @Output() submitted: EventEmitter<SportObject> = new EventEmitter<SportObject>();
  @Output() canceled = new EventEmitter();
  basicDataForm: FormGroup;
  address: Address;
  positionForm: FormGroup;
  openingTimeForm: FormGroup;
  positions: SportObjectPosition[] = [];
  private positionsToDelete: number[] = [];
  // openingTimes: OpeningTime[];

  get maxContentLength() {
    return 3000;
  }

  get maxPositionsCount() {
    return 10;
  }

  get mapZoom() {
    return 8;
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

    // this.initOpeningTimes();
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

  initOpeningTimeForm() {
    this.openingTimeForm = this._formBuilder.group({
      dayOfWeek: ['', Validators.required],
      openingTime: ['', Validators.required],
      closingTime: ['', Validators.required],
      timeInterval: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(1)]]
    });
  }

  addPosition() {
    this.positions.push(this.positionForm.value);
    this.resetPositionForm();
  }

  deletePosition(position) {
    this.positions = this.positions.filter(p => p!==position);
    if(position.id) {
      this.positionsToDelete.push(position.id);
    }
  }

  resetPositionForm() {
    const lastPositionsCount = this.positionForm.controls.positionsCount.value;
    this.positionForm.reset();
    this.positionForm.controls.positionsCount.setValue(lastPositionsCount);
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
