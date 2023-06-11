import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Score } from "./dashboard-student.model";

@Injectable({ providedIn: 'root' })
export class DashboardStudentService {

    constructor(private _http: HttpClient,) {

    }

    getQuestionsById(classId: string, token: string,) {
        const url = `http://localhost:8080/quiz/intrebari/by-clasa/${classId}`;
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


      saveQuestion(score: Score, token: string) {

        console.log("Test time " + score.testTimeInSec)
        const url = 'http://localhost:8080/quiz/salvare-punctaj';
        const httpOptions = {
            headers: new HttpHeaders({
              "Content-Type": "application/json",
              "Authorization": "Bearer " + token,
            }),
          };
        const body = JSON.stringify(score);
        return this._http.post<any>(url, body, httpOptions);
    }  
    


}
