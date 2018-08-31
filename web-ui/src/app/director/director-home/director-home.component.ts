import {Component, OnInit, ViewChild} from '@angular/core';
import {Announcement, AnnouncementService} from "../../common/http-service/announcement-service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PaginationComponent} from "../../common/component/pagination/pagination.component";
import {BaseComponent} from "../../common/base.component";
import {MatDialog} from "@angular/material";
import {TranslateService} from "@ngx-translate/core";
import {DefaultSort, SortField} from "../../common/component/pagination/listViewModel";
import {SortOrder} from "../../common/http-service/http-service.service";

@Component({
  selector: 'director-home',
  templateUrl: './director-home.component.html',
  styleUrls: ['./director-home.component.scss'],
  providers: [AnnouncementService],
})
export class DirectorHomeComponent extends BaseComponent implements OnInit {

  readonly pageSize: number = 10;
  announcements: Announcement[];
  announcementForm: FormGroup;
  announcementFormIsVisible: boolean;
  sortFields: SortField[];
  readonly defaultSort = new DefaultSort('lastModificationDate', SortOrder.DESC);

  @ViewChild(PaginationComponent)
  private paginationComponent: PaginationComponent<Announcement>;

  constructor(
    public announcementService: AnnouncementService,
    private translate: TranslateService,
    private formBuilder: FormBuilder,
    dialog: MatDialog) {
    super(dialog);
  }

  ngOnInit(): void {
    this.initAnnouncementForm();
    this.initSortFields();
  }

  get controls() {
    return this.announcementForm.controls;
  }

  initAnnouncementForm() {
    this.announcementForm = this.formBuilder.group({
      title: ['', Validators.required],
      content: ['', Validators.required]
    });
  }

  initSortFields() {
    let key = "pagination.sorting.announcement";

    this.translate.get([key]).subscribe((res: any) => {
      let value = res[key];
      this.sortFields = [
        {name: 'title', viewValue: value.title},
        {name: 'lastModificationDate', viewValue: value.lastModificationDate}
      ];
    });
  }

  showAnnouncementForm() {
    this.announcementFormIsVisible = true;
    window.scrollTo(0, 0);
  }

  hideAnnouncementForm() {
    this.announcementFormIsVisible = false;
    this.announcementForm.reset();
  }

  initAnnouncements(page) {
    this.announcements = page.content;
  }

  addAnnouncement() {
    this.announcementService.post(this.announcementForm.value).subscribe(
      (announcement) => {
        this.hideAnnouncementForm();
        this.paginationComponent.refreshPage();
      },
      (errorResponse) => {
        this.openConnectionErrorDialog();
      }
    );
  }
}
