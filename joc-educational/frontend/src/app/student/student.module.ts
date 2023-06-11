import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { NgChartsModule } from 'ng2-charts';
import { NgxEchartsModule } from 'ngx-echarts';
import { NgApexchartsModule } from 'ng-apexcharts';

import { StudentRoutingModule } from './student-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ComponentsModule } from '../shared/components/components.module';
import { SharedModule } from '../shared/shared.module';
import { QuestionComponent } from './dashboard/question/question.component';
import { AnswerCardComponent } from './dashboard/question/answer-card/answer-card.component';
import { QuestionReviewComponent } from './dashboard/question-review/question-review.component';

@NgModule({
  declarations: [DashboardComponent, QuestionComponent, AnswerCardComponent, QuestionReviewComponent],
  imports: [
    CommonModule,
    StudentRoutingModule,
    NgChartsModule,
    NgxEchartsModule.forRoot({
      echarts: () => import('echarts'),
    }),
    FormsModule,
    ReactiveFormsModule,
    NgScrollbarModule,
    NgApexchartsModule,
    ComponentsModule,
    SharedModule,
  ],
})
export class StudentModule {}
