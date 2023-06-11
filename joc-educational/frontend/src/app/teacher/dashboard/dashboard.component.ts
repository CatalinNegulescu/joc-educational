import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Class } from './dashboard-teacher.model';
import { DashboardTeacherService } from './dasboard-teacher.service';
import { take } from 'rxjs';
import { AuthService } from '@core';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {


  breadscrums = [
    {
      title: 'Dashboard',
      items: ['Profesor'],
      active: 'Dashboard',
    },
  ];

  quizForm!: FormGroup;
  classList: Class[] = [];
  submitted = false;

  constructor(private formBuilder: UntypedFormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private dashboardTeacherService: DashboardTeacherService,
    private authService: AuthService,) {
  }
 

  async ngOnInit(): Promise<void> {
    this.quizForm = this.formBuilder.group({
      classId: ['', Validators.required],
      question: ['', Validators.required],
      questionResponse1: ['', Validators.required],
      questionResponse2: ['', Validators.required],
      questionResponse3: ['', Validators.required],
      questionResponse4: ['', Validators.required],
      questionValidResponse: ['', Validators.required],
    });

    let token: any;
    if(this.checkExpireDateForAccessToken()){
      token = localStorage.getItem("tokenAcces");
      this.getClassList(token);
    } else {
      token = localStorage.getItem("tokenRefresh");
      await this.refreshToken(token);
    }
      
  

  }

  getClassList(token: string){
    this.dashboardTeacherService.getClassList(token).pipe(take(1)).subscribe(element => {
      console.log(element);
      this.classList = element;
    
    });
   }

   async onSubmit() {
    this.submitted = true;
    if (this.quizForm.invalid) {
      return;
    } else {
      
    let token: any;
    if(this.checkExpireDateForAccessToken()){
      token = localStorage.getItem("tokenAcces");
      this.saveQuestion(token);
    } else {
      token = localStorage.getItem("tokenRefresh");
      await this.refreshTokenBeforeSave(token);
    }
      
     
    }

 
  }
  saveQuestion(token: string){
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    this.dashboardTeacherService.saveQuestion(this.quizForm.value, token).pipe(take(1)).subscribe(element => {
      console.log("Intrebare salvata");
      //this.quizForm.reset();
      this.quizForm.reset({}, { onlySelf: true });
    });
  }




  refreshTokenBeforeSave(token: string){
    this.authService.refreshToken(token).pipe(take(1)).subscribe(element => {
      localStorage.setItem("tokenAcces", element.tokenAcces);
      localStorage.setItem("tokenRefresh", element.tokenRefresh);
      this.saveQuestion(element.tokenAcces);
    });
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
