import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import * as AuthActions from "../../store/auth/auth.actions";
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducers";
import {HttpError} from "../../store/app.reducers";
import {Observable} from "rxjs/Observable";
import * as PasswordValidators from "../../services/validators/password.validator";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signUpForm: FormGroup;
  emailPattern: string = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

  authState: Observable<{ errors: HttpError[], loading: boolean }>;


  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.signUpForm = new FormGroup({
      'email': new FormControl(null, [Validators.required, Validators.pattern(this.emailPattern)]),
      'passwordGroup': new FormGroup({
        'newPassword': new FormControl(null, [Validators.required, Validators.minLength(6)]),
        'newPasswordConfirm': new FormControl(null, [Validators.required, Validators.minLength(6)]),
      }, PasswordValidators.passwordMatchCheckValidator.bind(this))
    });

    this.authState = this.store.select('auth');
  }


  onSubmitted() {
    console.log(this.signUpForm);
    this.store.dispatch(new AuthActions.SignUp(
      {
        email: this.signUpForm.value.email,
        password: this.signUpForm.value.passwordGroup.newPassword,
        passwordRepeat: this.signUpForm.value.passwordGroup.newpasswordConfirm
      }));
  }

}
