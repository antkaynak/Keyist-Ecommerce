import { Action } from '@ngrx/store';
import { HttpError } from '../app.reducers';

export const SIGN_UP = 'SIGN_UP';
export const SIGN_UP_SUCCESS = 'SIGN_UP_SUCCESS';
export const SIGN_IN = 'SIGN_IN';
export const SIGN_IN_SUCCESS = 'SIGN_IN_SUCCESS';
export const SIGN_OUT = 'SIGN_OUT';
export const SIGN_OUT_SUCCESS = 'SIGN_OUT_SUCCESS';
export const CHECK_IF_LOGGED_IN = 'CHECK_IF_LOGGED_IN';
export const FETCH_VERIFICATION_STATUS = 'FETCH_VERIFICATION_STATUS';
export const FETCH_VERIFICATION_STATUS_SUCCESS = 'FETCH_VERIFICATION_STATUS_SUCCESS';
export const AUTH_ERROR = 'AUTH_ERROR';


export class SignUp implements Action {
  readonly type = SIGN_UP;

  constructor(public payload: { email: string, password: string, passwordRepeat: string }) {
  }
}

export class SignUpSuccess implements Action {
  readonly type = SIGN_UP_SUCCESS;

  constructor(public payload: { effect: string }) {
  }
}

export class SignIn implements Action {
  readonly type = SIGN_IN;

  constructor(public payload: { email: string, password: string }) {
  }
}

export class SignInSuccess implements Action {
  readonly type = SIGN_IN_SUCCESS;
  constructor(public payload: { effect: string }) {
  }
}

export class SignOut implements Action {
  readonly type = SIGN_OUT;
}

export class SignOutSuccess implements Action {
  readonly type = SIGN_OUT_SUCCESS;
}

export class CheckIfLoggedIn implements Action {
  readonly type = CHECK_IF_LOGGED_IN;
}

export class AuthError implements Action {
  readonly type = AUTH_ERROR;

  constructor(public payload: HttpError) {
  }
}

export class FetchVerificationStatus implements Action {
  readonly type = FETCH_VERIFICATION_STATUS;
}

export class FetchVerificationStatusSuccess implements Action {
  readonly type = FETCH_VERIFICATION_STATUS_SUCCESS;

  constructor(public payload: boolean) {
  }
}


export type AuthActions = SignUp | SignUpSuccess | SignIn | SignInSuccess
  | SignOut | SignOutSuccess | CheckIfLoggedIn
  | FetchVerificationStatus | FetchVerificationStatusSuccess
  | AuthError;
