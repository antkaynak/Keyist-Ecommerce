import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { Router } from '@angular/router';
import * as AuthActions from './auth.actions';
import * as CartActions from '../cart/cart.actions';
import * as OrderActions from '../order/order.actions';
import * as ShowcaseActions from '../showcase/showcase.actions';
import { TokenService } from '../../services/token.service';
import { AccountService } from '../../services/account.service';
import { of } from 'rxjs';
import { catchError, concatMap, map, switchMap } from 'rxjs/operators';


@Injectable()
export class AuthEffects {


  @Effect()
  signUp = this.actions$
    .pipe(ofType(AuthActions.SIGN_UP),
      map((action: AuthActions.SignUp) => {
        return action.payload;
      }),
      switchMap((credentials: { email: string, password: string, passwordRepeat: string }) => {
        return this.accountService.createAccount(credentials.email, credentials.password, credentials.passwordRepeat)
          .pipe(switchMap(res => {
            return [
              {
                type: AuthActions.SIGN_UP_SUCCESS, payload: { effect: AuthActions.SIGN_UP }
              }
              ,
              {
                type: AuthActions.SIGN_IN, // automatic sign in
                payload: { email: credentials.email, password: credentials.password }
              }
            ];
          }), catchError(error => of(new AuthActions.AuthError({ error, errorEffect: AuthActions.SIGN_UP }))));
      })
    );

  @Effect()
  signIn = this.actions$
    .pipe(ofType(AuthActions.SIGN_IN),
      map((action: AuthActions.SignIn) => {
        return action.payload;
      }),
      switchMap((credentials: { email: string, password: string, password2: string }) => {
        return this.tokenService.obtainAccessToken(credentials.email, credentials.password)
          .pipe(switchMap(res => {
            this.tokenService.saveToken(res);
            this.router.navigate(['/']);
            return [
              { type: AuthActions.SIGN_IN_SUCCESS, payload: { effect: AuthActions.SIGN_IN } },
              { type: AuthActions.FETCH_VERIFICATION_STATUS }
            ];
          }), catchError(error => of(new AuthActions.AuthError({ error, errorEffect: AuthActions.SIGN_IN }))));
      }));

  @Effect()
  signOut = this.actions$
    .pipe(ofType(AuthActions.SIGN_OUT),
      concatMap((action: AuthActions.SignOut) => {
        this.tokenService.removeToken();
        return [
          {
            type: AuthActions.SIGN_OUT_SUCCESS
          },
          {
            type: CartActions.EMPTY_CART_SUCCESS // clearing memory
          },
          {
            type: OrderActions.EMPTY_ORDER // clearing memory
          },
          {
            type: ShowcaseActions.EMPTY_INTERESTED // clearing memory
          }
        ];
      }));

  @Effect()
  checkIfLoggedIn = this.actions$
    .pipe(ofType(AuthActions.CHECK_IF_LOGGED_IN),
      switchMap((action: AuthActions.CheckIfLoggedIn) => {
        if (this.tokenService.checkIfTokenExists()) {
          return [
            {
              type: AuthActions.SIGN_IN_SUCCESS, payload: { effect: AuthActions.SIGN_IN_SUCCESS }
            },
            {
              type: AuthActions.FETCH_VERIFICATION_STATUS
            }
          ];
        }
        return [{
          type: AuthActions.SIGN_OUT_SUCCESS, payload: { effect: AuthActions.SIGN_OUT }
        }];
      }));

  @Effect()
  fetchVerificationStatus = this.actions$
    .pipe(ofType(AuthActions.FETCH_VERIFICATION_STATUS),
      switchMap((action: AuthActions.FetchVerificationStatus) => {
        return this.accountService.getVerificationStatus()
          .pipe(map(res => {
            return {
              type: AuthActions.FETCH_VERIFICATION_STATUS_SUCCESS, payload: res
            };
          }), catchError(error => of(new AuthActions.SignOut())));
      }));


  constructor(private actions$: Actions, private tokenService: TokenService,
              private router: Router, private accountService: AccountService) {
  }
}
