import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CustomerBookingService} from "../../http-service/customer-booking.service";
import {ConfirmationDialog} from "../../dialog/confirmation/confirmation.dialog";
import {ErrorHandlerService} from "../../error-handler.service";
import {MatDialog} from "@angular/material";
import {HttpErrorResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Booking, BookingState} from "../../http-service/booking.model";
import {BookingManagementService} from "../../http-service/booking-management.service";

@Component({
  selector: 'booking-item',
  templateUrl: './booking-item.component.html',
  styleUrls: ['./booking-item.component.scss'],
  providers: [CustomerBookingService, BookingManagementService]
})
export class BookingItemComponent {

  @Input() booking: Booking;
  @Input() isReceptionist: boolean;
  @Output() updated = new EventEmitter();
  bookingState = BookingState;

  private emitUpdate = () => this.updated.emit();
  private errorHandler = (error: HttpErrorResponse) => this.errorHandlerService.showDialog(this.dialog, error);

  constructor(private customerBookingService: CustomerBookingService,
              private bookingManagementService: BookingManagementService,
              private errorHandlerService: ErrorHandlerService,
              private dialog: MatDialog) {
  }

  showConfirmBookingDialog() {
    this.showConfirmDialog(this.bookingManagementService.confirm(this.booking.id));
  }

  showFinishBookingDialog() {
    this.showConfirmDialog(this.bookingManagementService.finish(this.booking.id));
  }

  showCancelBookingDialog() {
    this.showConfirmDialog(this.customerBookingService.cancel(this.booking.id));
  }

  showRejectBookingDialog() {
    this.showConfirmDialog(this.bookingManagementService.reject(this.booking.id));
  }

  private showConfirmDialog(bookingStateChange: Observable<void>) {
    this.dialog.open(ConfirmationDialog).afterClosed().subscribe(confirmed => {
      if (confirmed) {
        bookingStateChange.subscribe(this.emitUpdate, this.errorHandler);
      }
    });
  }
}
