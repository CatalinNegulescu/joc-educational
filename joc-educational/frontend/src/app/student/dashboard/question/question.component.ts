/* eslint-disable @typescript-eslint/no-inferrable-types */
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Question } from '../dashboard-student.model';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.scss']
})
export class QuestionComponent {
  @Input() question!: Question;
  @Input() isLastQuestion!: boolean;
  @Output() nextQuestion: EventEmitter<void> = new EventEmitter<void>();
  @Output() optionSelected: EventEmitter<string> = new EventEmitter<string>();

  selectedAnswer: string = '';

  selectOption(option: string) {
    this.selectedAnswer = option;
    console.log("option " )
    this.optionSelected.emit(option);
  }

  // handleOptionSelected(event: any){
  //   console.log("");
  // }
}
