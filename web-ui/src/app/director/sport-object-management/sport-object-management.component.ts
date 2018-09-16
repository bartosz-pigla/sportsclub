import {Component, OnInit, ViewChild} from '@angular/core';
import {AgmMap, MapsAPILoader} from "@agm/core";
import {Sportsclub, SportsclubService} from "../../common/http-service/sportsclub.service";
import {SportObject, SportObjectService} from "../../common/http-service/sport-object.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorHandlerService} from "../../common/error-handler.service";
import {MatDialog} from "@angular/material";

@Component({
  selector: 'sport-object-management',
  templateUrl: './sport-object-management.component.html',
  styleUrls: ['./sport-object-management.component.scss'],
  providers: [SportsclubService, SportObjectService]
})
export class SportObjectManagementComponent implements OnInit {

  sportObjects: SportObject[];
  sportObjectToEdit: SportObject;
  createFormIsVisible: boolean;
  sportsclub: Sportsclub;
  // @ViewChild(AgmMap) map: AgmMap;

  private readonly handleError = (error: HttpErrorResponse) => {
    this.errorHandlerService.showDialog(this.dialog, error);
  };

  constructor(
    private sportsclubService: SportsclubService,
    public sportObjectService: SportObjectService,
    private errorHandlerService: ErrorHandlerService,
    private dialog: MatDialog) {
  }

  ngOnInit() {
    this.initSportsclub();
    this.initSportObjects();
  }

  private initSportsclub() {
    this.sportsclubService.get(
      (sportsclub) => {
        console.log(`initSportsclub: ${JSON.stringify(sportsclub)}`);
        this.sportsclub = sportsclub;
      },
      (error) => {
        this.handleError(error);
      }
    )
  }

  private initSportObjects() {
    this.sportObjectService.get(
      (sportObjects) => {
        console.log(`get sport objects success: ${JSON.stringify(sportObjects)}`);
        this.sportObjects = sportObjects
      },
      (error) => {
        console.log('get sport objects fail');

        this.handleError(error);
      }
    );
  }

  deleteSportObject(sportObject) {
    this.sportObjects = this.sportObjects.filter(object => object !== sportObject);
  }

  showEditForm(sportObject) {
    this.sportObjectToEdit = sportObject;
  }

  hideEditForm() {
    this.sportObjectToEdit = null;
    this.initSportObjects();

  }

  showCreateForm() {
    this.createFormIsVisible = true;
    window.scrollTo(0, 0);
  }

  hideCreateForm() {
    this.createFormIsVisible = false;
    this.initSportObjects();
  }
}
