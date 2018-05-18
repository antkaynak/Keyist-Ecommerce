import * as AuthActions from './auth.actions';
import {HttpError} from "../app.reducers";

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  city: string;
  state: string;
  zip: string;
  emailVerified: number;
  registrationDate: string;
  phone: string;
  country: string;
  address: string;
  address2: string;
}


export interface State {
  authenticated: boolean;
  isActive: boolean;
  errors: HttpError[];
  loading: boolean;
}

const initialState: State = {
  authenticated: false,
  isActive: null,
  errors: [],
  loading: false
};

export function authReducer(state = initialState, action: AuthActions.AuthActions) {
  switch (action.type) {
    case(AuthActions.SIGN_IN):
    case(AuthActions.SIGN_OUT):
    case(AuthActions.SIGN_UP):
      return {
        ...state,
        loading: true
      };
    case (AuthActions.SIGN_UP_SUCCESS):
      let signUpErrorClear = state.errors;
      for (let i = 0; i < signUpErrorClear.length; i++) {
        if (signUpErrorClear[i].errorEffect === AuthActions.SIGN_UP) {
          signUpErrorClear = signUpErrorClear.splice(i, 1);
        }
      }
      return {
        ...state,
        errors: signUpErrorClear,
        loading: false
      };

    case (AuthActions.SIGN_IN_SUCCESS):
      let signInErrorClear = state.errors;
      for (let i = 0; i < signInErrorClear.length; i++) {
        if (signInErrorClear[i].errorEffect === AuthActions.SIGN_IN) {
          signInErrorClear = signInErrorClear.splice(i, 1);
        }
      }
      return {
        ...state,
        authenticated: true,
        errors: signInErrorClear,
        loading: false
      };
    case(AuthActions.AUTH_ERROR):
      let authErrorPush = state.errors;
      for (let i = 0; i < authErrorPush.length; i++) {
        if (authErrorPush[i].errorEffect === action.payload.errorEffect) {
          authErrorPush[i] = action.payload;
          return {
            ...state,
            errors: authErrorPush,
            loading: false
          };
        }
      }
      authErrorPush.push(action.payload);
      return {
        ...state,
        errors: authErrorPush,
        loading: false
      };

    case (AuthActions.SIGN_OUT_SUCCESS):
      return {
        authenticated: false,
        isActive: null,
        errors: [],
        loading: false
      };
    case (AuthActions.FETCH_VERIFICATION_STATUS_SUCCESS):
      return {
        ...state,
        isActive: action.payload
      };
    default:
      return state;
  }
}
