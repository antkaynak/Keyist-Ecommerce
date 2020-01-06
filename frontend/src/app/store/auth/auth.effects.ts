import {Injectable} from '@angular/core';
import {Actions, Effect, ofType} from '@ngrx/effects';
import {Router} from '@angular/router';
import * as AuthActions from "./auth.actions";
import * as CartActions from "../cart/cart.actions";
import * as OrderActions from "../order/order.actions";
import * as ShowcaseActions from "../showcase/showcase.actions";
import {TokenService} from "../../services/token.service";
import {AccountService} from "../../services/account.service";
import {of} from "rxjs";
import {catchError, concatMap, map, switchMap} from "rxjs/operators";


@Injectable()
export class AuthEffects {


  @Effect()
  signUp = this.actions$
    .pipe(ofType(AuthActions.SIGN_UP),
      map((action: AuthActions.SignUp) => {
        return action.payload
      }),
      switchMap((credentials: { email: string, password: string, passwordRepeat: string }) => {
        return this.accountService.createAccount(credentials.email, credentials.password, credentials.passwordRepeat)
          .pipe(switchMap(res => {
            console.log(res);
            return [
              {
                type: AuthActions.SIGN_UP_SUCCESS
              }
              ,
              {
                type: AuthActions.SIGN_IN, //automatic sign in
                payload: {email: credentials.email, password: credentials.password}
              }
            ]
          }), catchError(error => {
            return of(
              new AuthActions.AuthError(
                {error: error, errorEffect: AuthActions.SIGN_UP}));
          }));
      })
    );

  @Effect()
  signIn = this.actions$
    .pipe(ofType(AuthActions.SIGN_IN),
      map((action: AuthActions.SignIn) => {
        return action.payload
      }),
      switchMap((credentials: { email: string, password: string, password2: string }) => {
        return this.tokenService.obtainAccessToken(credentials.email, credentials.password)
          .pipe(switchMap(res => {
            this.tokenService.saveToken(res);
            this.router.navigate(["/account/dashboard"]);
            return [
              {type: AuthActions.SIGN_IN_SUCCESS},
              {type: AuthActions.FETCH_VERIFICATION_STATUS}
            ]
          }), catchError(error => {
          return of(
            new AuthActions.AuthError(
              {error: error, errorEffect: AuthActions.SIGN_IN}));
        }))

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
            type: CartActions.EMPTY_CART_SUCCESS //clearing memory
          },
          {
            type: OrderActions.EMPTY_ORDER //clearing memory
          },
          {
            type: ShowcaseActions.EMPTY_INTERESTED //clearing memory
          }
        ]
      }));

  @Effect()
  checkIfLoggedIn = this.actions$
    .pipe(ofType(AuthActions.CHECK_IF_LOGGED_IN),
      switchMap((action: AuthActions.CheckIfLoggedIn) => {
        if (this.tokenService.checkIfTokenExists()) {
          return [
            {
              type: AuthActions.SIGN_IN_SUCCESS
            },
            {
              type: AuthActions.FETCH_VERIFICATION_STATUS
            }
          ];
        } else {
          return [{
            type: AuthActions.SIGN_OUT_SUCCESS
          }]
        }
      }));


  @Effect()
  fetchVerificationStatus = this.actions$
    .pipe(ofType(AuthActions.FETCH_VERIFICATION_STATUS),
      switchMap((action: AuthActions.FetchVerificationStatus) => {
        return this.accountService.getVerificationStatus()
          .pipe(map(res => {
          return {
            type: AuthActions.FETCH_VERIFICATION_STATUS_SUCCESS, payload: res
          }
        }), catchError(error => {
          return of(new AuthActions.SignOut());
        }));
      }));


  constructor(private actions$: Actions, private tokenService: TokenService,
              private router: Router, private accountService: AccountService) {
  }
}
