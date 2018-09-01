import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Announcement, AnnouncementService} from "../../http-service/announcement-service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TranslateService} from "@ngx-translate/core";
import {MatDialog} from "@angular/material";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorHandlerService} from "../../error-handler.service";

@Component({
  selector: 'announcement-form',
  templateUrl: './announcement-form.component.html'
})
export class AnnouncementFormComponent implements OnInit {

  @Input() readonly initialAnnouncement: Announcement;
  @Output() announcementSubmitted: EventEmitter<Announcement> = new EventEmitter<Announcement>();
  @Output() canceled = new EventEmitter();
  announcementForm: FormGroup;

  get maxContentLength() {
    return 3000;
  }

  private readonly handleError = (error: HttpErrorResponse) => {
    this.errorHandlerService.showDialog(this.dialog, error);
  };

  constructor(
    public announcementService: AnnouncementService,
    private translate: TranslateService,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private errorHandlerService: ErrorHandlerService
    ) {
  }

  ngOnInit(): void {
    this.initAnnouncementForm();
  }

  get controls() {
    return this.announcementForm.controls;
  }

  initAnnouncementForm() {
    let titleValidator = Validators.required;
    let contentValidators = [Validators.required, Validators.maxLength(this.maxContentLength)];

    if (this.isEditForm()) {
      this.announcementForm = this.formBuilder.group({
        title: [this.initialAnnouncement.title, titleValidator],
        content: [this.initialAnnouncement.content, contentValidators]
      });
    } else {
      this.announcementForm = this.formBuilder.group({
        title: ['', titleValidator],
        content: ['', contentValidators]
      });
    }
  }

  addAnnouncement() {
    this.announcementService.post(this.announcementForm.value).subscribe(
      (announcement) => {
        this.announcementSubmitted.emit(announcement);
      },
      (error) => {
        this.handleError(error);
      }
    );
  }

  editAnnouncement() {
    let id = this.initialAnnouncement.id;
    let editedAnnouncement: Announcement = this.announcementForm.value;

    this.announcementService.put(id, editedAnnouncement).subscribe(
      () => {
        this.announcementSubmitted.emit(new Announcement(
          id,
          editedAnnouncement.title,
          editedAnnouncement.content,
          this.initialAnnouncement.lastModificationDate))
      },
      (error) => {
        this.handleError(error);
      }
    );

  }

  cancel() {
    this.canceled.emit();
  }

  isEditForm() {
    return this.initialAnnouncement;
  }
}
