import * as AuthActions from './auth.actions';
import { HttpError } from '../app.reducers';


export interface AuthState {
  authenticated: boolean;
  isActive: boolean;
  errors: Array<HttpError>;
  loading: boolean;
}

const initialState: AuthState = {
  authenticated: false,
  isActive: null,
  errors: [],
  loading: false
};

export function authReducer(state = initialState, action: AuthActions.AuthActions) {
  switch (action.type) {
    case (AuthActions.SIGN_IN):
    case (AuthActions.SIGN_OUT):
    case (AuthActions.SIGN_UP):
      return {
        ...state,
        loading: true
      };
    case (AuthActions.SIGN_UP_SUCCESS):
      return {
        ...state,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: false
      };

    case (AuthActions.SIGN_IN_SUCCESS):
      return {
        ...state,
        authenticated: true,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: false
      };

    case (AuthActions.AUTH_ERROR):
      const errors = [...state.errors];
      const index = errors.findIndex(error => error.errorEffect === action.payload.errorEffect);
      if (index !== -1) {
        errors[index] = action.payload;
      } else {
        errors.push(action.payload);
      }
      return {
        ...state,
        loading: false,
        errors
      };

    case (AuthActions.SIGN_OUT_SUCCESS):
      return initialState;

    case (AuthActions.FETCH_VERIFICATION_STATUS_SUCCESS):
      return {
        ...state,
        isActive: action.payload
      };
    default:
      return state;
  }
}
