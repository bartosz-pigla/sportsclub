import {Injectable} from "@angular/core";
import {BookingDetail, BookingDetailWithOpeningTimeAndPosition} from "./http-service/booking-service";

export class SessionDate {
  constructor(public year: number,
              public month: number,
              public day: number) {

  }

  public static createFrom(date: Date): SessionDate {
    return new SessionDate(date.getFullYear(), date.getMonth(), date.getDate());
  }

  public static dateEquals(currentDate: SessionDate, date: Date) {
    return currentDate.year === date.getFullYear() && currentDate.month === date.getMonth() && currentDate.day === date.getDate();
  }

  public static equals(currentDate: SessionDate, date: SessionDate) {
    return currentDate.year === date.year && currentDate.month === date.month && currentDate.day === date.day;
  }
}

export class SessionBookingDetail {
  constructor(public sportObjectPositionId: string,
              public openingTimeId: string,
              public date: SessionDate,
              public sportObjectId: string) {

  }

  public static equals(currentDetail: SessionBookingDetail, detail: SessionBookingDetail) {
    return currentDetail.sportObjectPositionId === detail.sportObjectPositionId &&
      currentDetail.openingTimeId === detail.openingTimeId &&
      SessionDate.equals(currentDetail.date, detail.date) &&
      currentDetail.sportObjectId === detail.sportObjectId;
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
    sessionDetails.push(new SessionBookingDetail(detail.sportObjectPositionId, detail.openingTimeId, SessionDate.createFrom(detail.date), objectId));
    sessionStorage.setItem(BookingSummaryService.bookingDetailsKey, JSON.stringify(sessionDetails));
  }

  delete(detail: SessionBookingDetail) {
    let sessionDetails = this.getDetails();
    if (sessionDetails !== null) {
      sessionDetails = sessionDetails.filter(d => !SessionBookingDetail.equals(d, detail));
      sessionStorage.setItem(BookingSummaryService.bookingDetailsKey, JSON.stringify(sessionDetails));
    }
  }

  deleteAll() {
    sessionStorage.removeItem(BookingSummaryService.bookingDetailsKey);
  }

  getDetails(): SessionBookingDetail[] {
    let sessionDetails = sessionStorage.getItem(BookingSummaryService.bookingDetailsKey);
    return sessionDetails ? JSON.parse(sessionDetails) : null;
  }

  detailExists(detail: BookingDetailWithOpeningTimeAndPosition, objectId: string, date: Date) {
    const details = this.getDetails();
    return details === null ? false : details.findIndex(d =>
      SessionDate.dateEquals(d.date, date) && d.openingTimeId === detail.openingTimeId && d.sportObjectPositionId === detail.positionId) !== -1;
  }

  detailsExists(): boolean {
    let sessionDetails = JSON.parse(sessionStorage.getItem(BookingSummaryService.bookingDetailsKey));
    return sessionDetails !== null && sessionDetails.length > 0;
  }
}
