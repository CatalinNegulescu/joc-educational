import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-answer-card',
  templateUrl: './answer-card.component.html',
  styleUrls: ['./answer-card.component.scss']
})
export class AnswerCardComponent {
  
  @Input() option1!: string;
  @Output() optionSelected: EventEmitter<string> = new EventEmitter<string>();
  @Input() isSelected!: boolean;

  selectOption() {
    this.optionSelected.emit(this.option1);
  }

}