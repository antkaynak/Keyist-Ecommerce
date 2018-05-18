import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AccountService} from "../../services/account.service";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/catch';
import * as fromApp from "../../store/app.reducers";
import {Store} from "@ngrx/store";
import * as AuthActions from "../../store/auth/auth.actions"

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.css']
})
export class EmailVerificationComponent implements OnInit {
  isVerified: boolean = null;
  isLoggedIn: boolean = null;


  constructor(private route: ActivatedRoute, private accountService: AccountService, private store: Store<fromApp.AppState>) {
  }

  ngOnInit() {
    const verificationToken = this.route.snapshot.queryParams["token"];
    this.accountService.verifyEmail(verificationToken).take(1).catch(error => {
      this.isVerified = false;
      return Observable.throw(error);
    }).subscribe((data) => {
      this.isVerified = true;
      this.store.select('auth').take(1).subscribe(data => {
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
