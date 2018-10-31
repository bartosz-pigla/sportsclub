import {Component, Input} from '@angular/core';
import {Announcement} from "../../http-service/announcement-service";

@Component({
  selector: 'announcement-item',
  templateUrl: './announcement-item.component.html',
  styleUrls: ['./announcement-item.component.scss']
})
export class AnnouncementItemComponent {

  @Input() announcement: Announcement;
}
