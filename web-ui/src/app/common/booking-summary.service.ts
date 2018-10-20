import {Injectable} from "@angular/core";
import {BookingDetail, BookingDetailWithOpeningTimeAndPosition} from "./http-service/booking-service";
import {dateEquals} from "./http-service/opening-time-service";

export class SessionBookingDetail {
  constructor(public bookingDetail: BookingDetail,
              public sportObjectId: string) {

  }

  // detailAndSportObjectIdAndDateEquals(detail: BookingDetailWithOpeningTimeAndPosition, objectId: string, date: Date) {
  //   return this.sportObjectId === objectId &&
  //     this.bookingDetail.openingTimeId === detail.openingTimeId &&
  //     this.bookingDetail.sportObjectPositionId === detail.positionId &&
  //     dateEquals(this.bookingDetail.date, date)
  // }

  public static detailAndSportObjectIdAndDateEquals(currentDetail: SessionBookingDetail, detail: BookingDetailWithOpeningTimeAndPosition, objectId: string, date: Date) {
    return currentDetail.sportObjectId === objectId &&
      currentDetail.bookingDetail.openingTimeId === detail.openingTimeId &&
      currentDetail.bookingDetail.sportObjectPositionId === detail.positionId &&
      dateEquals(currentDetail.bookingDetail.date, date)
  }

  // detailAndSportObjectIdEquals(detail: BookingDetail, objectId: string) {
  //   return this.bookingDetail.equals(detail) && this.sportObjectId === objectId;
  // }

  // detailEquals(detail: BookingDetail) {
  //   return this.bookingDetail.equals(detail);
  // }

  public static detailEquals(currentDetail: SessionBookingDetail, detail: BookingDetail) {
    return currentDetail.bookingDetail.equals(detail);
  }
}

@Injectable({
  providedIn: 'root',
})
export class BookingSummaryService {

  private static bookingDetailsKey = 'bookingDetails';

  addDetail(detail: BookingDetail, objectId: string) {
    let sessionDetails = this.getDetails();
    if (sessionDetails === null) {
      sessionDetails = [];
    }
    sessionDetails.push(new SessionBookingDetail(detail, objectId));
    sessionStorage.setItem(BookingSummaryService.bookingDetailsKey, JSON.stringify(sessionDetails));
  }

  delete(detail: BookingDetail) {
    let sessionDetails = this.getDetails();
    if (sessionDetails !== null) {
      sessionDetails = sessionDetails.filter(d => SessionBookingDetail.detailEquals(d, detail));
      sessionStorage.setItem(BookingSummaryService.bookingDetailsKey, JSON.stringify(sessionDetails));
    }
  }

  getDetails(): SessionBookingDetail[] {
    let sessionDetails = sessionStorage.getItem(BookingSummaryService.bookingDetailsKey);
    return sessionDetails ? JSON.parse(sessionDetails) : null;
  }

  detailExists(detail: BookingDetailWithOpeningTimeAndPosition, objectId: string, date: Date) {
    const details = this.getDetails();
    return details === null ? false : details.findIndex(d => {
      return SessionBookingDetail.detailAndSportObjectIdAndDateEquals(d, detail, objectId, date)
    }) !== -1;
  }

  detailsExists(): boolean {
    let sessionDetails = JSON.parse(sessionStorage.getItem(BookingSummaryService.bookingDetailsKey));
    return sessionDetails !== null && sessionDetails.length > 0;
  }
}
