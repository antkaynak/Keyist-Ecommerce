import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AccountService} from "../../services/account.service";
import {throwError} from "rxjs";
import {catchError, take} from "rxjs/operators";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html'
})
export class ForgotPasswordComponent implements OnInit {

  forgotPasswordForm: FormGroup;
  emailPattern: string = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.forgotPasswordForm = new FormGroup({
      'email': new FormControl(null, [Validators.required, Validators.pattern(this.emailPattern)])
    });
  }

  onForgotPasswordFormSubmit() {
    console.log(this.forgotPasswordForm);
    this.accountService.forgotPasswordRequest(this.forgotPasswordForm.value.email)
      .pipe(take(1), catchError(
        error => {
          alert("An error occurred. Please try again.");
          return throwError(error);
        }
      ))
      .subscribe(res => {
        alert("Success! A verification link has been sent if an account exists with this email.");
      });
    this.forgotPasswordForm.reset();
  }

}
