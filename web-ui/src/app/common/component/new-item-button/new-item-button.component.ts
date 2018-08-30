import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'new-item-button',
  templateUrl: './new-item-button.component.html',
  styleUrls: ['./new-item-button.component.scss']
})
export class NewItemButtonComponent {

  @Output() buttonClick = new EventEmitter();

  click() {
    this.buttonClick.emit();
  }
}
