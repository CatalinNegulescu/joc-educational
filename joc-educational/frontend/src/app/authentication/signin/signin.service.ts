import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { LoginUser } from "./signin.model";



@Injectable({ providedIn: 'root' })
export class SignInService {

    constructor(private _http: HttpClient,) {

    }

    loginUser(loginUser: LoginUser) {
      const url = 'http://localhost:8080/utilizator/login';
        const httpOptions = {
          headers: new HttpHeaders({
            "Content-Type": "application/json",
          }),
        };
        const options = {withCredentials: true, 'access-control-allow-origin': "http://localhost:4200/", 'Content-Type': 'application/json'}
        const body = JSON.stringify(loginUser);
        return this._http.post<any>(url, body, options);
    }
    
}