/* eslint-disable @typescript-eslint/no-non-null-assertion */
/* eslint-disable prefer-const */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-empty-function */
/* eslint-disable @typescript-eslint/no-inferrable-types */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexYAxis,
  ApexStroke,
  ApexTooltip,
  ApexDataLabels,
  ApexPlotOptions,
  ApexResponsive,
  ApexGrid,
  ApexLegend,
  ApexFill,
} from 'ng-apexcharts';
import { Question, QuestionCheck, QuestionModel, Score } from './dashboard-student.model';
import { FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { AuthService } from '@core';
import { take } from 'rxjs';
import { DashboardTeacherService } from 'app/teacher/dashboard/dasboard-teacher.service';
import { Class } from 'app/teacher/dashboard/dashboard-teacher.model';
import { DashboardStudentService } from './dashboard-student.service';

export type barChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  responsive: ApexResponsive[];
  xaxis: ApexXAxis;
  grid: ApexGrid;
  legend: ApexLegend;
  fill: ApexFill;
};

export type areaChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  stroke: ApexStroke;
  tooltip: ApexTooltip;
  dataLabels: ApexDataLabels;
  legend: ApexLegend;
  grid: ApexGrid;
  colors: string[];
};

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {

  breadscrums = [
    {
      title: 'Dashboard',
      items: ['Student'],
      active: 'Dashboard',
    },
  ];

  questions: Question[] = [
    {
      questionText: 'Care dintre următoarele este capitala României?',
      options: ['A) București', 'B) Cluj-Napoca', 'C) Iași', 'D) Timișoara'],
      correctOption: 'A) București'
    },
    {
      questionText: 'Care este cel mai înalt vârf din lume?',
      options: ['A) Mount Everest', 'B) K2', 'C) Kangchenjunga', 'D) Makalu'],
      correctOption: 'A) Mount Everest'
    },
    {
      questionText: 'Cine a scris romanul "Mândrie și prejudecată"?',
      options: ['A) Jane Austen', 'B) Charlotte Brontë', 'C) Emily Brontë', 'D) Louisa May Alcott'],
      correctOption: 'A) Jane Austen'
    }
  ];
  questionsForReview: QuestionCheck[] = [];
  currentQuestionIndex: number = 0;
  score: number = 0;
  startTime!: number;
  endTime!: number;
  isTestComplete: boolean = false;
  selectedOption: string | null = null;

  startingTest:boolean = false;
  testForm!: FormGroup;
  private timerInterval: any;
  private readonly updateInterval = 1000;
  timeElapsed = '';
  timeElapsedInMin: number = 0;
  correctOptionWasSelected: boolean = false;
  lastOptionSelected: string = '';
  classList: Class[] = [];
  reviewTest:boolean = false;
  scoreRecord: Score = {
    classId: '',
    score: '',
    testTimeInSec: ''
  };

  constructor(private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private dashboardTeacherService: DashboardTeacherService,
    private dashboardStudentService: DashboardStudentService,
    ) {
    //constructor
  }


  ngOnInit() {
    // this.testForm = this.formBuilder.group({
    //   classId: ['', Validators.required],
    // });
    this.testForm = new FormGroup({
      classId: new FormControl('', Validators.required)
    });
    this.getListOfClassesForStudent();
    console.log("");
  
  }


  ngOnDestroy() {
    this.stopTimer();
  }

  async getListOfClassesForStudent(){
    let token: any;
    if(this.checkExpireDateForAccessToken()){
      token = localStorage.getItem("tokenAcces");
      this.getClassList(token);
    } else {
      token = localStorage.getItem("tokenRefresh");
      await this.refreshToken(token);
    }
  }

  

  nextQuestion() {
    this.questionsForReview.push({
      questionText: this.questions[this.currentQuestionIndex].questionText,
      options: this.questions[this.currentQuestionIndex].options,
      correctOption: this.questions[this.currentQuestionIndex].correctOption,
      itWasMarkedAsCorrect: this.correctOptionWasSelected,
      lastOptionSelected: this.lastOptionSelected
    })
    if(this.correctOptionWasSelected){
      this.score++;
    }
    if (this.currentQuestionIndex < this.questions.length - 1) {
      this.currentQuestionIndex++;
    } else {
      this.isTestComplete = true;
      this.endTime = Date.now();
    }

    this.selectedOption = null;
  }

  calculateScore(selectedOption: string, correctOption: string) {

    this.lastOptionSelected = selectedOption;
    if (selectedOption === correctOption) {
      this.correctOptionWasSelected = true;
     // this.score++;
    }else{
      this.correctOptionWasSelected = false;
    }
  }

 startTest() {
    this.currentQuestionIndex = 0;
    this.startTime = Date.now();
    this.endTime = 0;
    this.score = 0;
    this.timeElapsed = '';
    this.startTimer();
    this.selectedOption = null;
    this.questionsForReview = [];
    
   
  }


  startTimer() {
    this.timerInterval = setInterval(() => {
      this.calculateTimeElapsed();
    }, this.updateInterval);
  }

  
  stopTimer() {
    clearInterval(this.timerInterval);
  }

  isLastQuestion(): boolean {
    return this.currentQuestionIndex === this.questions.length - 1;
  }


  finishTest() {
    if(this.correctOptionWasSelected){
      this.score++;
    }
    this.questionsForReview.push({
      questionText: this.questions[this.currentQuestionIndex].questionText,
      options: this.questions[this.currentQuestionIndex].options,
      correctOption: this.questions[this.currentQuestionIndex].correctOption,
      itWasMarkedAsCorrect: this.correctOptionWasSelected,
      lastOptionSelected: this.lastOptionSelected
    })
    this.isTestComplete = true;
    this.endTime = Date.now();
    this.stopTimer();
    this.calculateTimeElapsed();
    this.scoreRecord.score = this.score.toString();
    this.scoreRecord.classId = this.testForm.controls['classId']?.value;

    this.saveScore();
  }

  calculateTimeElapsed() {
    const elapsedMilliseconds = this.endTime ? this.endTime - this.startTime! : Date.now() - this.startTime!;
    const elapsedSeconds = Math.floor(elapsedMilliseconds / 1000);
    const minutes = Math.floor(elapsedSeconds / 60);
    const seconds = elapsedSeconds % 60;
    this.timeElapsed = `${minutes} minute și ${seconds} secunde`;
    
    this.scoreRecord.testTimeInSec = elapsedSeconds.toString();
  }
  
  getTest(){

    this.getListOfQuestionsForStudent();
  }

  getClassList(token: string){
    this.dashboardTeacherService.getClassList(token).pipe(take(1)).subscribe(element => {
      console.log(element);
      this.classList = element;
    
    });
   }

  saveScoreRecord(token:string){
    this.dashboardStudentService.saveQuestion(this.scoreRecord, token).pipe(take(1)).subscribe(element => {

    });

  } 

   getQuestionList(token: string){
    this.dashboardStudentService.getQuestionsById(this.testForm.controls['classId']?.value, token).pipe(take(1)).subscribe(listOfQuestions => {
      console.log(listOfQuestions);
    

      listOfQuestions.forEach((question: QuestionModel, index: any) => {
        let auxQuestion: Question = {
          questionText: '',
          options: [],
          correctOption: ''
        };
        auxQuestion.questionText = question.text;
        auxQuestion.options.push(question.variantaRaspunsA);
        auxQuestion.options.push(question.variantaRaspunsB);
        auxQuestion.options.push(question.variantaRaspunsC);
        auxQuestion.options.push(question.variantaRaspunsD);
        if(question.variantaRaspunsCorect == 'A')
          auxQuestion.correctOption = question.variantaRaspunsA;
        if(question.variantaRaspunsCorect == 'B')
          auxQuestion.correctOption = question.variantaRaspunsB;  
        if(question.variantaRaspunsCorect == 'C')
          auxQuestion.correctOption = question.variantaRaspunsC;
        if(question.variantaRaspunsCorect == 'D')
          auxQuestion.correctOption = question.variantaRaspunsD;  
        this.questions.push(auxQuestion);   
        
        if (index === listOfQuestions.length - 1) {
          this.startTest();
          this.startingTest = true;
        }
      });
    });
    
   }


   getOtherTest(){
    this.startingTest = false;
    this.isTestComplete = false;
    this.questions = [];
    this.reviewTest = false;
   }
   
   getReviewTest(){
      this.reviewTest = true;
   }

   async getListOfQuestionsForStudent(){
    let token: any;
    if(this.checkExpireDateForAccessToken()){
      token = localStorage.getItem("tokenAcces");
      this.getQuestionList(token);
    } else {
      token = localStorage.getItem("tokenRefresh");
      await this.refreshTokenQuestionList(token);
    }
  }

  async saveScore(){
    let token: any;
    if(this.checkExpireDateForAccessToken()){
      token = localStorage.getItem("tokenAcces");
      this.saveScoreRecord(token);
    } else {
      token = localStorage.getItem("tokenRefresh");
      await this.refreshTokenSaveScore(token);
    }
  }

  async refreshToken(token: string){
    this.authService.refreshToken(token).pipe(take(1)).subscribe(element => {
     console.log("modificare token");
     console.log(element);

     localStorage.setItem("tokenAcces", element.tokenAcces);
     localStorage.setItem("tokenRefresh", element.tokenRefresh);
     this.getClassList(element.tokenAcces);
   });
  }

  async refreshTokenQuestionList(token: string){
    this.authService.refreshToken(token).pipe(take(1)).subscribe(element => {
     console.log("modificare token");
     console.log(element);

     localStorage.setItem("tokenAcces", element.tokenAcces);
     localStorage.setItem("tokenRefresh", element.tokenRefresh);
     this.getQuestionList(element.tokenAcces);
   });
  }

  async refreshTokenSaveScore(token: string){
    this.authService.refreshToken(token).pipe(take(1)).subscribe(element => {
     console.log("modificare token");
     console.log(element);

     localStorage.setItem("tokenAcces", element.tokenAcces);
     localStorage.setItem("tokenRefresh", element.tokenRefresh);
     this.saveScoreRecord(element.tokenAcces);
   });
  }

  checkExpireDateForAccessToken(): boolean{
   
    if( localStorage.getItem("tokenAcces")){
      const aux = localStorage.getItem("tokenAcces")?.toString();
      if(aux == undefined){
        return true;
      }
      const jwtToken = JSON.parse(atob(aux.split('.')[1]));
      console.log(new Date(jwtToken.exp * 1000))
      const expiry = new Date(jwtToken.exp * 1000);
      const nowDate = new Date(Date.now());
      const isExpired =  expiry > nowDate;
      if(isExpired)
        return true;
    }
 
    return false;
  }

  checkExpireDateForRefreshToken(): boolean{
    if( localStorage.getItem("tokenRefresh")){
      const aux = localStorage.getItem("tokenRefresh")?.toString();
      if(aux == undefined){
        return true;
      }
      const jwtToken = JSON.parse(atob(aux.split('.')[1]));
      console.log(new Date(jwtToken.exp * 1000))
      const expiry = new Date(jwtToken.exp * 1000);
      const nowDate = new Date(Date.now());
      const isExpired =  expiry > nowDate;
      if(isExpired)
        return true;
    }
    return false;
  }
  


}
