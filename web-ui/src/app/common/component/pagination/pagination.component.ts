import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {BaseComponent} from "../../base.component";
import {IPageableAndSortableGetService, SortingParams, SortOrder} from "../../http-service/http-service.service";
import {SortField} from "./listViewModel";

@Component({
  selector: 'pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss'],
})
export class PaginationComponent<T> extends BaseComponent implements OnInit {

  @Input() private readonly pageSize: number;
  @Input() private readonly service: IPageableAndSortableGetService<T>;
  @Input() readonly sortFields: SortField[];
  @Input() readonly defaultSort: SortingParams;

  @Output() pageChange: EventEmitter<ListViewModel<T>> = new EventEmitter<ListViewModel<T>>();

  listModel: ListViewModel<T> = ListViewModel.createEmpty(this.pageSize);

  private readonly emitPageChange = () => {
    this.pageChange.emit(this.page);
  };

  constructor(private router: Router,
              dialog: MatDialog) {
    super(dialog);
  }

  ngOnInit() {
    this.nextPage();
  }

  refreshPage() {
    this.page.refresh(this.service, this.emitPageChange, this.openDefaultErrorDialog);
  }

  nextPage() {
    this.page.next(this.service, this.emitPageChange, this.openDefaultErrorDialog);
  }

  previousPage() {
    this.page.previous(this.service, this.emitPageChange, this.openDefaultErrorDialog);
  }

  foo(obj) {
    console.log(this.sortField);
  }
}
