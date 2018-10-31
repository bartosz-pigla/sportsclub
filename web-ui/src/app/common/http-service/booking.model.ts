import {dateEquals, Time} from "../date-time.utils";
import {User} from "./user.service";

export class BookingDetailWithOpeningTimeAndPosition {
  constructor(
    public positionId: string,
    public name: string,
    public bookedPositionsCount: number,
    public positionsCount: number,
    public openingTimeId: string,
    public startTime: Time,
    public finishTime: Time,
    public price: number) {
  }
}

export class BookingDetail {
  constructor(
    public bookingDetailId: string,
    public sportObjectPositionId: string,
    public openingTimeId: string,
    public date: Date) {
  }

  static createFrom(detail: BookingDetailWithOpeningTimeAndPosition, date: Date) {
    return new BookingDetail('', detail.positionId, detail.openingTimeId, date);
  }

  equals(detail: BookingDetail) {
    return this.sportObjectPositionId === detail.sportObjectPositionId &&
      this.openingTimeId === detail.openingTimeId &&
      dateEquals(this.date, detail.date);
  }
}

export class Booking {
  constructor(
    public id: string,
    public date: string,
    public state: BookingState,
    public customer?: User) {
  }
}

export enum BookingState {
  CREATED = "CREATED",
  SUBMITTED = "SUBMITTED",
  CONFIRMED = "CONFIRMED",
  CANCELED = "CANCELED",
  REJECTED = "REJECTED",
  FINISHED = "FINISHED"
}

export namespace BookingState {

  export function canConfirm(state: BookingState): boolean {
    return state === BookingState.SUBMITTED;
  }

  export function canFinish(state: BookingState) {
    return state === BookingState.CONFIRMED;
  }

  export function canCancel(state: BookingState): boolean {
    return state === BookingState.CREATED || state === BookingState.SUBMITTED || state === BookingState.CONFIRMED;
  }

  export function canReject(state: BookingState) {
    return state === BookingState.SUBMITTED;
  }
}
