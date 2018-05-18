import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Store} from "@ngrx/store";
import * as fromApp from "../store/app.reducers";
import * as AuthActions from '../store/auth/auth.actions';
import {TokenService} from "./token.service";
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/finally';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/take';
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {Router} from "@angular/router";


@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  isRefreshingToken: boolean = false;
  tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>(null);

  constructor(private store: Store<fromApp.AppState>, private tokenService: TokenService, private router: Router) {
  }

  addTokenToHeader(request: HttpRequest<any>, token): HttpRequest<any> {
    if (token != null) {
      return request.clone({setHeaders: {Authorization: 'Bearer ' + token.access_token}})
    }
    return request.clone({setHeaders: {Authorization: 'Bearer ' + this.tokenService.getToken()}})
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url.includes('/secured/')) {
      return next.handle(this.addTokenToHeader(request, null)).catch(error => {
        if (error && error.status === 400 && error.error && error.error.error === 'invalid_grant') {
          // If we get a 400 and the error message is 'invalid_grant', the token is no longer valid so logout.
          this.store.dispatch(new AuthActions.SignOut());
          return Observable.of(new AuthActions.SignOut());
        }

        if (error && error.status === 401 && error.error && error.error.error === 'invalid_token') {
          return this.handle401Error(request, next);
        }

        return Observable.throw(error);
      });

    } else {
      return next.handle(request);
    }

  }

  handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshingToken) {
      this.isRefreshingToken = true;

      this.tokenSubject.next(null);

      return this.tokenService.obtainAccessTokenWithRefreshToken(this.tokenService.getRefreshToken())
        .switchMap((newToken: string) => {
          if (newToken) {
            const oldToken = this.tokenService.getToken();
            if (oldToken == null || oldToken == '') { //If user logs out while we are obtaining token
              this.store.dispatch(new AuthActions.SignOut());
              return Observable.of(new AuthActions.SignOutSuccess());
            }
            this.tokenService.saveToken(newToken);
            this.tokenSubject.next(newToken);
            return next.handle(this.addTokenToHeader(request, newToken));
          }

          this.store.dispatch(new AuthActions.SignOut());
          return Observable.of(); //if code gets here the effect that dispatches it becomes inactive.
        })
        .catch(error => {
          this.router.navigate(["/login"]);
          return Observable.of(new AuthActions.SignOut());
        })
        .finally(() => {
          this.isRefreshingToken = false;
        });
    } else {
      return this.tokenSubject
        .filter(newToken => newToken != null)
        .take(1)
        .switchMap(newToken => {
          return next.handle(this.addTokenToHeader(request, newToken));
        }).catch(error => {
          this.store.dispatch(new AuthActions.SignOut());
          return Observable.of(new AuthActions.SignOut());
        });
    }
  }

}
