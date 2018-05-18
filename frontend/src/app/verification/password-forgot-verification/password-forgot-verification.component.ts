import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AccountService} from "../../services/account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/filter';
import * as BlankValidators from "../../services/validators/blank.validator";
import * as PasswordValidators from "../../services/validators/password.validator";
import * as fromApp from "../../store/app.reducers";
import {Store} from "@ngrx/store";
import * as AuthActions from '../../store/auth/auth.actions';
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-password-forgot-verification',
  templateUrl: './password-forgot-verification.component.html',
  styleUrls: ['./password-forgot-verification.component.css']
})
export class PasswordForgotVerificationComponent implements OnInit, OnDestroy {

  isVerified: boolean;
  authSubscription: Subscription;

  forgotPasswordResetForm: FormGroup;
  passwordForgotToken: string;

  constructor(private store: Store<fromApp.AppState>, private route: ActivatedRoute, private router: Router, private accountService: AccountService) {
  }

  ngOnInit() {
    this.authSubscription = this.store.select('auth')
      .filter(data => data.authenticated)
      .subscribe(data => {
        this.store.dispatch(new AuthActions.SignOut());
      });
    this.forgotPasswordResetForm = new FormGroup({
      'newPasswordGroup': new FormGroup({
        'newPassword': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.minLength(6)]),
        'newPasswordConfirm': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.minLength(6)])
      }, PasswordValidators.passwordMatchCheckValidator),
    });


    this.passwordForgotToken = this.route.snapshot.queryParams["token"];
    this.accountService.forgotPasswordConfirm(this.passwordForgotToken).take(1).catch(error => {
      this.isVerified = false;
      return Observable.throw(error);
    }).subscribe((data) => {
      this.isVerified = true;
    });
  }

  ngOnDestroy() {
    if (this.authSubscription != null) {
      this.authSubscription.unsubscribe();
    }
  }

  onForgotPasswordResetFormSubmit() {
    this.accountService.forgotPasswordReset(
      this.passwordForgotToken,
      this.forgotPasswordResetForm.value.newPasswordGroup.newPassword,
      this.forgotPasswordResetForm.value.newPasswordGroup.newPasswordConfirm
    ).take(1)
      .catch(error => {
        alert("An error occurred. Please try again.");
        return Observable.throw(error);
      })
      .subscribe(res => {
        alert("Success! \nYou have changed your password. Please login.");
        this.router.navigate(["/login"]);
      });

  }

}
