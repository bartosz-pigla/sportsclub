import {Component, OnInit, ViewChild} from '@angular/core';
import {Announcement, AnnouncementService} from "../../common/http-service/announcement-service";
import {SortField} from "../../common/component/list-view/list-view.model";
import {SortingParams, SortOrder} from "../../common/http-service/http-service.service";
import {ListViewComponent} from "../../common/component/list-view/list-view.component";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'home-page',
  templateUrl: './public-home.component.html',
  styleUrls: ['./public-home.component.scss'],
  providers: [AnnouncementService]
})
export class PublicHomeComponent implements OnInit {

  pageSize: number;
  announcements: Announcement[];
  sortFields: SortField[];
  defaultSort: SortingParams;

  @ViewChild(ListViewComponent)
  private paginationComponent: ListViewComponent<Announcement>;

  constructor(
    public announcementService: AnnouncementService,
    private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.initSortFields();
    this.pageSize = 10;
    this.defaultSort = new SortingParams('lastModificationDate', SortOrder.DESC);
  }

  initSortFields() {
    this.translate.get("listView.sorting.announcement").subscribe((res: any) => {
      this.sortFields = [
        {name: 'title', viewValue: res['title']},
        {name: 'lastModificationDate', viewValue: res['publicationDate']}
      ];
    });
  }

  initAnnouncements(announcements) {
    this.announcements = announcements;
  }
}
