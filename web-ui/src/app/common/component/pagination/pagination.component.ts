import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PageView} from "../../page-view";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {BaseComponent} from "../../base.component";
import {IPageableAndSortableGetService} from "../../http-service/http-service.service";

@Component({
  selector: 'pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss'],
})
export class PaginationComponent<T> extends BaseComponent implements OnInit {

  @Input() private readonly pageSize: number = 10;
  @Input() private readonly service: IPageableAndSortableGetService<T>;

  @Output() pageChange: EventEmitter<PageView<T>> = new EventEmitter<PageView<T>>();

  page: PageView<T> = PageView.createEmpty(this.pageSize);

  constructor(private router: Router,
              dialog: MatDialog) {
    super(dialog);
  }

  ngOnInit() {
    console.log(this.pageSize);
    console.log('paagination');
    this.nextPage();
    this.pageChange.emit(this.page);
  }

  nextPage() {
    this.page.next(this.service, () => {
      this.openConnectionErrorDialog();
    });
    this.pageChange.emit(this.page);
  }

  previousPage() {
    this.page.previous(this.service, () => {
      this.openConnectionErrorDialog();
    });
    this.pageChange.emit(this.page);
  }
}
