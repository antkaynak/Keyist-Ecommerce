import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../../services/account.service';
import * as fromApp from '../../../store/app.reducers';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as BlankValidators from '../../../../utils/validators/blank.validator';
import * as AuthActions from '../../../store/auth/auth.actions';
import { throwError } from 'rxjs';
import { catchError, take } from 'rxjs/operators';

@Component({
  selector: 'app-reset-details',
  templateUrl: './reset-details.component.html',
  styleUrls: ['./reset-details.component.scss']
})
export class ResetDetailsComponent implements OnInit {

  detailsForm: FormGroup;
  innerLoading = true;

  constructor(private accountService: AccountService, private store: Store<fromApp.AppState>, private router: Router) { }

  ngOnInit() {

    this.detailsForm = new FormGroup({
      firstName: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(52)]),
      lastName: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(52)]),
      phone: new FormControl(null, [BlankValidators.checkIfBlankValidator, Validators.pattern('[0-9]+'), Validators.minLength(11), Validators.maxLength(12)]),
    });

    this.accountService.getUser().pipe(take(1), catchError(error => {
      this.store.dispatch(new AuthActions.SignOut());
      this.router.navigate(['/']);
      return throwError(error);
    }
    )).subscribe(data => {
      this.detailsForm.patchValue({
        firstName: data.firstName,
        lastName: data.lastName,
        phone: data.phone,
      });

      this.innerLoading = false;
    });
  }

  onSubmitDetailsForm() {
    this.innerLoading = true;


    const user = {
      firstName: this.detailsForm.value?.firstName?.trim()?.length ? this.detailsForm.value.firstName.trim() : null,
      lastName: this.detailsForm.value?.lastName?.trim()?.length ? this.detailsForm.value.lastName.trim() : null,
      phone: this.detailsForm.value?.phone?.length ? this.detailsForm.value.phone : null,
    };

    this.accountService.updateUser(user)
      .pipe(take(1), catchError(
        error => {
          alert('An error occurred. Please refresh your page.');
          return throwError(error);
        }
      ))
      .subscribe(data => {
        this.innerLoading = false;
        alert('Success! Your information has been changed.');
      });

  }


}
