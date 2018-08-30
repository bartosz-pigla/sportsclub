import {Component, Input, OnInit} from '@angular/core';
import {PageView} from "../../page-view";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {BaseComponent} from "../../base.component";
import {Announcement, AnnouncementService} from "../../http-service/announcement-service";

@Component({
  selector: 'announcement-list',
  templateUrl: './announcement-list.component.html',
  styleUrls: ['./announcement-list.component.scss'],
  providers: [AnnouncementService]
})
export class AnnouncementListComponent extends BaseComponent implements OnInit {

  @Input() readonly isReadOnly: boolean = true;
  private readonly pageSize: number = 10;
  page: PageView<Announcement> = PageView.createEmpty(this.pageSize);
  public newAnnouncementFormIsVisible: boolean;

  constructor(private router: Router,
              private announcementService: AnnouncementService,
              dialog: MatDialog) {
    super(dialog);
  }

  ngOnInit() {
    this.nextPage();
  }

  nextPage() {
    this.page.next(this.announcementService, () => {
      this.openConnectionErrorDialog();
    })
  }

  previousPage() {
    this.page.previous(this.announcementService, () => {
      this.openConnectionErrorDialog();
    })
  }

  showNewAnnouncementForm() {
    this.newAnnouncementFormIsVisible = true;
    window.scrollTo(0, 0);
  }
}
