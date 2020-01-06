import {Component, OnDestroy, OnInit} from '@angular/core';
import {AccountService} from "../../../services/account.service";
import * as fromApp from "../../../store/app.reducers";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import * as BlankValidators from "../../../services/validators/blank.validator";
import * as AuthActions from "../../../store/auth/auth.actions";
import {Subscription, throwError} from "rxjs";
import {catchError, take} from "rxjs/operators";

@Component({
  selector: 'app-reset-details',
  templateUrl: './reset-details.component.html'
})
export class ResetDetailsComponent implements OnInit, OnDestroy {

  detailsForm: FormGroup;
  formHidden : boolean = true;
  inlineLoading: boolean = false;
  formLoading: boolean = true;
  formSubscription: Subscription;

  constructor(private accountService : AccountService, private store: Store<fromApp.AppState>,private router: Router) { }

  ngOnInit() {
    this.detailsForm = new FormGroup({
      'firstName': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(26)]),
      'lastName': new FormControl(null,[Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(26)]),
      'address': new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'),BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(240)]),
      'address2': new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'),BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(240)]),
      'city': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(100)]),
      'state': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(40)]),
      'zip': new FormControl(null, [ Validators.pattern('^[0-9]*$'), Validators.maxLength(6),Validators.minLength(5)]),
      'country': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(40)]),
      'phone': new FormControl(null, [Validators.pattern('[0-9]+'), Validators.minLength(11), Validators.maxLength(12)]),
    });


   this.formSubscription = this.accountService.getUser()
     .pipe(catchError(
       error => {
         this.formLoading = false;
         return throwError(error);
       }
     ))
     .subscribe( data => {
      if(data != null) {
        this.formLoading = false;
        this.formHidden = false;
        this.detailsForm.patchValue({
          firstName: data.firstName,
          lastName: data.lastName,
          address: data.address,
          address2: data.address2,
          city: data.city,
          state: data.state,
          zip: data.zip,
          country: data.country,
          phone: data.phone
        });
      }else{
        this.store.dispatch(new AuthActions.SignOut());
        this.router.navigate(["/"]);
      }
    });
  }

  ngOnDestroy() {
    if(this.formSubscription != null){
      this.formSubscription.unsubscribe();
    }
  }

  onSubmitDetailsForm() {
    this.inlineLoading = true;

    const user = {
      firstName: this.detailsForm.value.firstName,
      lastName: this.detailsForm.value.lastName,
      address: this.detailsForm.value.address,
      address2: this.detailsForm.value.address2,
      city: this.detailsForm.value.city,
      state: this.detailsForm.value.state,
      zip: this.detailsForm.value.zip,
      country: this.detailsForm.value.country,
      phone: this.detailsForm.value.phone,
    };

    this.accountService.saveUser(user)
      .pipe(take(1), catchError(
        error => {
          this.formHidden = true;
          this.inlineLoading = false;
          alert("An error occurred while fetching user data. Please refresh your page.");
          return throwError(error);
        }
      ))
      .subscribe(data => {
        this.inlineLoading = false;
    });

  }


}
