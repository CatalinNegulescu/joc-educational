import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { LoginUser } from "../signin/signin.model";
import { RegisterUser } from "./signup.model";

@Injectable({ providedIn: 'root' })
export class SignUpService {

    constructor(private _http: HttpClient,) {

    }

    registerNewUser(registerUser: RegisterUser) {
        const url = 'http://localhost:8080/utilizator/creare-cont';
        const httpOptions = {
            headers: new HttpHeaders({
              "Content-Type": "application/json",
            }),
          };
        //const options = {withCredentials: true, 'access-control-allow-origin': "http://localhost:4200/", 'Content-Type': 'application/json'}
        const body = JSON.stringify(registerUser);
        return this._http.post<any>(url, body, httpOptions);
    }
    
}