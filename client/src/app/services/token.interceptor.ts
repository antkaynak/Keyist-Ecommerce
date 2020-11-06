import { Injectable } from '@angular/core';
import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Store } from '@ngrx/store';
import * as fromApp from '../store/app.reducers';
import * as AuthActions from '../store/auth/auth.actions';
import { TokenService } from './token.service';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { catchError, filter, finalize, switchMap, take } from 'rxjs/operators';


@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  isRefreshingToken = false;
  tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>(null);

  constructor(private store: Store<fromApp.AppState>, private tokenService: TokenService, private router: Router) {
  }

  addTokenToHeader(request: HttpRequest<any>, token): HttpRequest<any> {
    return request.clone({ setHeaders: { Authorization: `Bearer ${token ? token : this.tokenService.getToken()}` } });
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): any {
    if (!request.url.includes('/public/') && !request.url.includes('oauth')) {
      return next.handle(this.addTokenToHeader(request, null)).pipe(catchError(
        error => {
          if (error && error.status === 400 && error.error && error.error.error === 'invalid_grant') {
            // If we get a 400 and the error message is 'invalid_grant', the token is no longer valid so logout.
            this.store.dispatch(new AuthActions.SignOut());
          }

          if (error && error.status === 401 && error.error && error.error.error === 'invalid_token') {
            return this.handle401Error(request, next);
          }

          return throwError(error);
        }
      ));

    } else {
      return next.handle(request);
    }

  }

  handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshingToken) {
      this.isRefreshingToken = true;

      this.tokenSubject.next(null);

      return this.tokenService.obtainAccessTokenWithRefreshToken(this.tokenService.getRefreshToken())
        .pipe(switchMap((newToken: string) => {
          if (newToken) {
            if (!this.tokenService.checkIfTokenExists()) { // If user logs out while we are obtaining token
              this.store.dispatch(new AuthActions.SignOut());
              return of();
            }
            this.tokenService.saveToken(newToken);
            this.tokenSubject.next(newToken);
            return next.handle(this.addTokenToHeader(request, newToken));
          }

          this.store.dispatch(new AuthActions.SignOut());
          return of(); // if code gets here the effect that dispatches it becomes inactive.
        }), catchError(() => {
          this.router.navigate(['/login']);
          return of(new AuthActions.SignOut());
        }), finalize(() => {
          this.isRefreshingToken = false;
        }));
    } else {
      return this.tokenSubject
        .pipe(
          filter(newToken => newToken !== null),
          take(1),
          switchMap(newToken => {
            return next.handle(this.addTokenToHeader(request, newToken));
          }), catchError(() => {
            this.store.dispatch(new AuthActions.SignOut());
            return of();
          }));
    }
  }

}
