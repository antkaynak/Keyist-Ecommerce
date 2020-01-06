import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AccountService} from "../../services/account.service";
import {throwError} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducers";
import {catchError, take} from "rxjs/operators";

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-reset-verification.component.html',
  styleUrls: ['./email-reset-verification.component.css']
})
export class EmailResetVerificationComponent implements OnInit {
  isVerified: boolean = null;
  isLoggedIn: boolean = null;

  constructor(private route: ActivatedRoute, private accountService: AccountService, private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    const emailResetToken = this.route.snapshot.queryParams["token"];
    this.accountService.resetEmailConfirm(emailResetToken).pipe(take(1), catchError(error => {
      this.isVerified = false;
      return throwError(error);
    })).subscribe((data) => {
      this.isVerified = true;
      this.store.select('auth').pipe(take(1)).subscribe(data => {
        this.isLoggedIn = data.authenticated;
      });
    });
  }
}
