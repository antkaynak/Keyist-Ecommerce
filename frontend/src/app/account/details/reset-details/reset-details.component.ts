import {Component, OnDestroy, OnInit} from '@angular/core';
import {AccountService} from "../../../services/account.service";
import * as fromApp from "../../../store/app.reducers";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import * as BlankValidators from "../../../services/validators/blank.validator";
import * as AuthActions from "../../../store/auth/auth.actions";
import {Observable} from "rxjs/Observable";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-reset-details',
  templateUrl: './reset-details.component.html',
  styleUrls: ['./reset-details.component.css']
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
      'firstName': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator]),
      'lastName': new FormControl(null,[Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator]),
      'address': new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'),BlankValidators.checkIfBlankValidator]),
      'address2': new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'),BlankValidators.checkIfBlankValidator]),
      'city': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator]),
      'state': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator]),
      'zip': new FormControl(null, [ Validators.maxLength(6),Validators.minLength(5)]),
      'country': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'),BlankValidators.checkIfBlankValidator]),
      'phone': new FormControl(null, [Validators.pattern('[0-9]+'), Validators.minLength(11), Validators.maxLength(12)]),
    });


   this.formSubscription = this.accountService.getUser().catch(error => {
      this.formLoading = false;
      return Observable.throw(error);
    }).subscribe( data => {
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
        console.log("data is null");
        console.log(data);
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
    console.log(this.detailsForm);
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

    this.accountService.saveUser(user).take(1).catch(error => {
      this.formHidden = true;
      this.inlineLoading = false;
      alert("An error occurred while fetching user data. Please refresh your page.");
      return Observable.throw(error);
    }).subscribe(data => {
        this.inlineLoading = false;
    });

  }


}
