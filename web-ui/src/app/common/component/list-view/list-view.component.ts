import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {IPageableAndSortableGetService, SortingParams} from "../../http-service/http-service.service";
import {ListViewModel, SortField} from "./list-view.model";
import {ErrorHandlerService} from "../../error-handler.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'list-view',
  templateUrl: './list-view.component.html',
  styleUrls: ['./list-view.component.scss'],
})
export class ListViewComponent<T> implements OnInit {

  @Input() readonly title: string;
  @Input() private readonly pageSize: number;
  @Input() private readonly service: IPageableAndSortableGetService<T>;
  @Input() readonly sortFields: SortField[];
  @Input() defaultSort: SortingParams;
  @Input() searchParams: T;
  @Output() pageChange: EventEmitter<T[]> = new EventEmitter<T[]>();

  listModel: ListViewModel<T>;

  private readonly emitPageChange = () => {
    this.pageChange.emit(this.listModel.content);
  };

  private readonly handleError = (error: HttpErrorResponse) => {
    this.errorHandlerService.showDialog(this.dialog, error);
  };

  constructor(private router: Router,
              private dialog: MatDialog,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.listModel = ListViewModel.createEmpty(this.pageSize, this.defaultSort, this.searchParams);
    this.refreshPage();
  }

  refreshPage() {
    this.listModel.refresh(this.service, this.emitPageChange, this.handleError);
  }

  nextPage() {
    this.listModel.next(this.service, this.emitPageChange, this.handleError);
  }

  previousPage() {
    this.listModel.previous(this.service, this.emitPageChange, this.handleError);
  }
}
