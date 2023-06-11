import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Class, QuestionSave } from "./dashboard-teacher.model";


@Injectable({ providedIn: 'root' })
export class DashboardTeacherService {

    constructor(private _http: HttpClient,) {

    }

  

    getClassList(token: string) {
        const url = 'http://localhost:8080/clasa-elevi/all';
          const httpOptions =  {
            headers: new HttpHeaders({
              "Content-Type": "application/json",
              "Authorization": "Bearer " + token,
              'access-control-allow-origin': "http://localhost:4200/",
              'Access-Control-Allow-Methods': "GET, OPTIONS",
              'Access-Control-Allow-Credentials':'true',
              'Access-Control-Allow-Headers':'Authorization, Content-Type, enctype'
    
            })};
          return this._http.get<any>(url, httpOptions);
      }

    
    saveQuestion(question: QuestionSave, token: string) {

      const url = 'http://localhost:8080/quiz/salvare-intrebare';
      const httpOptions = {
          headers: new HttpHeaders({
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token,
          }),
        };
      const body = JSON.stringify(question);
      return this._http.post<any>(url, body, httpOptions);
  }  




  
}

