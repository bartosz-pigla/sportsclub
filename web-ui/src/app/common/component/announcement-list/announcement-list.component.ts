import {Component, Input, OnInit} from '@angular/core';
import {PageView} from "../../page-view";
import {Announcement, AnnouncementService} from "../../announcement-service";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {ConnectionErrorDialog} from "../connection-error-dialog/connection-error-dialog.component";
import {BaseComponent} from "../../base.component";

@Component({
  selector: 'announcement-list',
  templateUrl: './announcement-list.component.html',
  styleUrls: ['./announcement-list.component.scss']
})
export class AnnouncementListComponent extends BaseComponent implements OnInit {

  @Input() isReadOnly: boolean = true;
  page: PageView<Announcement>;

  constructor(private router: Router,
              private announcementService: AnnouncementService,
              dialog: MatDialog) {
    super(dialog);
  }

  ngOnInit() {
    this.announcementService.get().subscribe(
      (announcements) => {
        this.page = PageView.of(this.announcementService.pageSize, announcements);
      },
      () => {
        this.openConnectionErrorDialog();
      }
    )
  }

  nextPage() {
    if (this.page.hasNext()) {
      this.page.next(
        this.announcementService.get(this.page.currentPage + 1),
        () => {
          this.openConnectionErrorDialog();
        });
    }
  }

  previousPage() {
    if (this.page.hasPrevious()) {
      this.page.previous(
        this.announcementService.get(this.page.currentPage - 1),
        () => {
          this.openConnectionErrorDialog();
        });
    }
  }
}
