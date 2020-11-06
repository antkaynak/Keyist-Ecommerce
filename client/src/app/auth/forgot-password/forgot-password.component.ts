import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { throwError } from 'rxjs';
import { catchError, take } from 'rxjs/operators';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  forgotPasswordForm: FormGroup;
  emailPattern = '^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$';
  innerLoading = false;

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.forgotPasswordForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.pattern(this.emailPattern)])
    });
  }

  onForgotPasswordFormSubmit() {
    this.innerLoading = true;
    this.accountService.forgotPasswordRequest(this.forgotPasswordForm.value.email)
      .pipe(take(1), catchError(
        error => {
          this.innerLoading = false;
          alert('An error occurred. Please try again.');
          return throwError(error);
        }
      ))
      .subscribe(res => {
        this.innerLoading = false;
        this.forgotPasswordForm.reset();
        alert('Success! A verification link has been sent if an account exists with this email.');
      });

  }

}
