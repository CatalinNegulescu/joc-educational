import { Component, Input } from '@angular/core';
import { QuestionCheck } from '../dashboard-student.model';

@Component({
  selector: 'app-question-review',
  templateUrl: './question-review.component.html',
  styleUrls: ['./question-review.component.scss']
})
export class QuestionReviewComponent {
  @Input() questionsForReview: QuestionCheck[] = [];
}
