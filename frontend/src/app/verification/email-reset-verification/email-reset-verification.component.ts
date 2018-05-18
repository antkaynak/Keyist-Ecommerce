import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AccountService} from "../../services/account.service";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/catch';
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducers";

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
    this.accountService.resetEmailConfirm(emailResetToken).take(1).catch(error => {
      this.isVerified = false;
      return Observable.throw(error);
    }).subscribe((data) => {
      this.isVerified = true;
      this.store.select('auth').take(1).subscribe(data => {
        if (data.authenticated) {
          this.isLoggedIn = true;
        } else {
          this.isLoggedIn = false;
        }
      });
    });
  }

}
