import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'abstract-info-dialog',
  templateUrl: './abstract-info.dialog.html'
})
export class AbstractInfoDialog {

  @Input()
  title: string;
  @Input()
  content: string;
  @Output() closed = new EventEmitter();

  close() {
    this.closed.emit();
  }
}
