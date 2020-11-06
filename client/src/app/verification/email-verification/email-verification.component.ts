import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { throwError } from 'rxjs';
import * as fromApp from '../../store/app.reducers';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../store/auth/auth.actions';
import { catchError, take } from 'rxjs/operators';

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.scss']
})
export class EmailVerificationComponent implements OnInit {

  isVerified: boolean = null;
  isError: boolean = null;
  isLoggedIn: boolean = null;

  constructor(private route: ActivatedRoute, private accountService: AccountService, private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    const verificationToken = this.route.snapshot.queryParams.token;
    this.accountService.verifyEmail(verificationToken).pipe(take(1), catchError(error => {
      this.isVerified = false;
      this.isError = true;
      return throwError(error);
    })).subscribe((data) => {
      this.isVerified = true;
      this.store.select('auth').pipe(take(1)).subscribe(data => {
        if (data.authenticated) {
          this.isLoggedIn = true;
          this.store.dispatch(new AuthActions.FetchVerificationStatus());
        } else {
          this.isLoggedIn = false;
        }
      });
    });
  }
}
