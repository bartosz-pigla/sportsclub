import { Component, OnInit } from '@angular/core';
import {PageView} from "../../common/page-view";
import {Announcement, AnnouncementService} from "../../common/http-service/announcement-service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'director-home',
  templateUrl: './director-home.component.html',
  styleUrls: ['./director-home.component.scss'],
  providers: [AnnouncementService]
})
export class DirectorHomeComponent implements OnInit{

  readonly pageSize: number = 10;

  page: PageView<Announcement>;
  announcementForm: FormGroup;
  announcementFormIsVisible: boolean;

  constructor(
    public announcementService: AnnouncementService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.initAnnouncementForm();
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

  showAnnouncementForm() {
    this.announcementFormIsVisible = true;
    window.scrollTo(0, 0);
  }

  hideAnnouncementForm() {
    this.announcementFormIsVisible = false;
  }

  changePage(page) {
    console.log('change page: ' + this.page.content);
    this.page = page;
  }

  addAnnouncement() {
    this.announcementService.post(this.announcementForm.value).subscribe(
      (announcement) => {
        console.log(announcement);
      },
      (errorResponse) => {

      }
    );
  }
}
