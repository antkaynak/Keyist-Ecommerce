import { AccountService } from 'src/app/services/account.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as BlankValidators from '../../../utils/validators/blank.validator';
import { catchError, take } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.scss']
})
export class AddressComponent implements OnInit {

  addressForm: FormGroup;
  innerLoading = true;

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.addressForm = new FormGroup({
      address: new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(240)]),
      city: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(100)]),
      state: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(40)]),
      zip: new FormControl(null, [Validators.pattern('^[0-9]*$'), BlankValidators.checkIfBlankValidator, Validators.maxLength(6), Validators.minLength(5)]),
      country: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(40)])
    });

    this.accountService.getUser().pipe(take(1)).subscribe(data => {
      this.addressForm.patchValue({
        address: data.address,
        city: data.city,
        state: data.state,
        zip: data.zip,
        country: data.country
      });

      this.innerLoading = false;
    });
  }

  onSubmitAddressForm() {
    this.innerLoading = true;

    const user = {
      address: this.addressForm.value?.address?.trim()?.length ? this.addressForm.value.address.trim() : null,
      city: this.addressForm.value?.city?.trim()?.length ? this.addressForm.value.city.trim() : null,
      state: this.addressForm.value?.state?.trim()?.length ? this.addressForm.value.state.trim() : null,
      zip: this.addressForm.value?.zip?.trim()?.length ? this.addressForm.value.zip.trim() : null,
      country: this.addressForm.value?.country?.trim()?.length ? this.addressForm.value.country.trim() : null
    };

    this.accountService.updateUserAddress(user)
      .pipe(take(1), catchError(
        error => {
          alert('An error occurred. Please refresh your page.');
          return throwError(error);
        }
      ))
      .subscribe(data => {
        this.innerLoading = false;
        alert('Success! Your address has been changed.');
      });

  }

}
