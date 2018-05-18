import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducers";
import {HttpError} from "../../store/app.reducers";
import * as AuthActions from '../../store/auth/auth.actions';
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/take'
import 'rxjs/add/operator/filter'


@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  signInForm: FormGroup;
  emailPattern: string = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

  authState: Observable<{ errors: HttpError[], loading: boolean }>;


  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    this.signInForm = new FormGroup({
      'email': new FormControl(null, [Validators.required, Validators.pattern(this.emailPattern)]),
      'password': new FormControl(null, Validators.required),
    });

    this.authState = this.store.select('auth');

  }


  onSubmitted() {
    this.store.dispatch(new AuthActions.SignIn({
      email: this.signInForm.value.email,
      password: this.signInForm.value.password
    }));
  }

}
