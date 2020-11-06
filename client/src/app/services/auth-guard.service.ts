import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Store } from '@ngrx/store';
import * as fromApp from '../store/app.reducers';
import * as fromAuth from '../store/auth/auth.reducer';
import { map, take } from 'rxjs/operators';

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(private store: Store<fromApp.AppState>, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.store.select('auth')
      .pipe(take(1), map((authState: fromAuth.AuthState) => {
        if (!authState.authenticated) {
          this.router.navigate(['/login']);
        }
        return authState.authenticated;
      }));
  }
}
