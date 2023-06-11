import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { Role, AuthService, User } from '@core';
import { UnsubscribeOnDestroyAdapter } from '@shared';
import { SignInService } from './signin.service';
import { take } from 'rxjs';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss'],
})
export class SigninComponent
  extends UnsubscribeOnDestroyAdapter
  implements OnInit
{
  authForm!: UntypedFormGroup;
  submitted = false;
  loading = false;
  error = '';
  hide = true;
  constructor(
    private formBuilder: UntypedFormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private signInService: SignInService,
  ) {
    super();
  }

  ngOnInit() {
    this.authForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }
  get f() {
    return this.authForm.controls;
  }
 
  onSubmit() {
    this.submitted = true;
    this.loading = true;
    this.error = '';
    if (this.authForm.invalid) {
      this.error = 'Username-ul sau parola nu sunt completate !';
      return;
    } else {

      console.log("incep login")

      
    this.signInService.loginUser(this.authForm.value).pipe(take(1)).subscribe(  element =>{
      console.log(element);
      console.log(element.tokenAcces);
      localStorage.setItem('tokenAcces', element.tokenAcces);
      localStorage.setItem('tokenRefresh', element.tokenRefresh);
      
      console.log(JSON.parse(atob(element.tokenAcces.split('.')[1])));
      const jwtToken = JSON.parse(atob(element.tokenAcces.split('.')[1]));
      localStorage.setItem('username', jwtToken.sub);
      console.log(new Date(jwtToken.exp * 1000))
      const expiry = new Date(jwtToken.exp * 1000);
      const nowDate = new Date(Date.now());
      const isExpired =  expiry > nowDate;
      console.log(isExpired);


      this.authService.userRole = jwtToken.roluri[0];
      console.log(this.authService.userRole);
      localStorage.setItem('rol', jwtToken.roluri[0]);
      const role = jwtToken.roluri[0];
      localStorage.setItem('currentUser', JSON.stringify(jwtToken.sub));
      // const userData: User = {
      //   id: '',
      //   img: '',
      //   username: jwtToken.sub,
      //   password: '',
      //   firstName: jwtToken.sub,
      //   lastName: '',
      //   role: role,
      //   token: ''
      // };
      // console.log(userData);
      // this.authService.setCurrentUserValue({
      //   id: '',
      //   img: '',
      //   username: 'jwtToken.sub',
      //   password: '',
      //   firstName: '',
      //   lastName: '',
      //   role: role,
      //   token: element.tokenAcces});

      console.log("ROL: " + role)
      if (role === Role.All || role === Role.Admin) {
        this.router.navigate(['/admin/dashboard/main']);
      } else if (role === Role.Teacher) {
        this.router.navigate(['/teacher/dashboard']);
      } else if (role === Role.Student) {
        this.router.navigate(['/student/dashboard']);
      } else {
        this.router.navigate(['/authentication/signin']);
      }
      this.loading = false;
      // console.log(this.jwtHelper.getTokenExpirationDate(localStorage.getItem('tokenAcces')));
      // console.log(this.jwtHelper.decodeToken(localStorage.getItem('tokenAcces')));
      // console.log(this.jwtHelper.getTokenExpirationDate(element.tokenAcces));
      // console.log(this.jwtHelper.decodeToken(element.tokenAcces));
      
     // console.log(element.tokenAcces);
      // if(element == false){
      //   Swal.fire({
      //     icon: 'error',
      //     title: 'Credentiale gresite',
      //     text: 'Va rugam sa introduceti datele corecte!',
      //     footer: '<a href="">Daca considerati ca este o greseala, contactati administratorul!</a>'
      //   })
      // }else{
        Swal.fire({
          icon: 'success',
          title: 'Credentiale corecte',
         
        })
       // this.router.navigateByUrl('/error404');
    //  }
   
  }, err => {
    //console.log(err)
    if(err.status == 403){
      console.log("403");
      Swal.fire({
            icon: 'error',
            title: 'Credentiale gresite',
            text: 'Va rugam sa introduceti datele corecte!',
            footer: '<a href="">Daca considerati ca este o greseala, contactati administratorul!</a>'
          })
          this.authForm.reset();
          this.loading = false;
    }

  }
  );

      // this.subs.sink = this.authService
      //   .login(this.f['username'].value, this.f['password'].value)
      //   .subscribe({
      //     next: (res) => {
      //       if (res) {
      //         setTimeout(() => {
      //           const role = this.authService.currentUserValue.role;
      //           if (role === Role.All || role === Role.Admin) {
      //             this.router.navigate(['/admin/dashboard/main']);
      //           } else if (role === Role.Teacher) {
      //             this.router.navigate(['/teacher/dashboard']);
      //           } else if (role === Role.Student) {
      //             this.router.navigate(['/student/dashboard']);
      //           } else {
      //             this.router.navigate(['/authentication/signin']);
      //           }
      //           this.loading = false;
      //         }, 1000);
      //       } else {
      //         this.error = 'Invalid Login';
      //       }
      //     },
      //     error: (error) => {
      //       this.error = error;
      //       this.submitted = false;
      //       this.loading = false;
      //     },
      //   });
    }
  }
}
