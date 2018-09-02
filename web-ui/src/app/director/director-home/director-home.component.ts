import {Component, OnInit, ViewChild} from '@angular/core';
import {Announcement, AnnouncementService} from "../../common/http-service/announcement-service";
import {ListViewComponent} from "../../common/component/list-view/list-view.component";
import {TranslateService} from "@ngx-translate/core";
import {SortField} from "../../common/component/list-view/list-view.model";
import {SortingParams, SortOrder} from "../../common/http-service/http-service.service";

@Component({
  selector: 'director-home',
  templateUrl: './director-home.component.html',
  styleUrls: ['./director-home.component.scss'],
  providers: [AnnouncementService],
})
export class DirectorHomeComponent implements OnInit {

  pageSize: number;
  announcements: Announcement[];
  createAnnouncementFormIsVisible: boolean;
  sortFields: SortField[];
  defaultSort: SortingParams;
  announcementToEdit: Announcement;

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
    let key = "list-view.sorting.announcement";

    this.translate.get([key]).subscribe((res: any) => {
      let value = res[key];
      this.sortFields = [
        {name: 'title', viewValue: value['title']},
        {name: 'lastModificationDate', viewValue: value['lastModificationDate']}
      ];
    });
  }

  showCreateAnnouncementForm() {
    this.createAnnouncementFormIsVisible = true;
    window.scrollTo(0, 0);
  }

  hideCreateAnnouncementForm() {
    this.createAnnouncementFormIsVisible = false;
    this.refreshPage();
  }

  refreshPage() {
    this.paginationComponent.refreshPage();
  }

  initAnnouncements(announcements) {
    this.announcements = announcements;
  }

  showEditForm(announcement) {
    this.announcementToEdit = announcement;
  }

  hideEditAnnouncementForm() {
    this.announcementToEdit = null;
    this.paginationComponent.refreshPage();
  }

  foo() {
    console.log('foo');
    this.pageSize = 1;
  }
}
