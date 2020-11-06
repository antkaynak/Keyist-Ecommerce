import { User } from './../../store/model';
import * as OrderActions from './../../store/order/order.actions';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { take } from 'rxjs/operators';
import { AccountService } from 'src/app/services/account.service';
import * as BlankValidators from '../../../utils/validators/blank.validator';
import * as fromApp from '../../store/app.reducers';
import { OrderState } from 'src/app/store/order/order.reducer';

@Component({
  selector: 'app-shipping',
  templateUrl: './shipping.component.html',
  styleUrls: ['./shipping.component.scss']
})
export class ShippingComponent implements OnInit {

  shippingForm: FormGroup;
  billingAddressEnabled = false;

  innerLoading = true;

  constructor(private store: Store<fromApp.AppState>, private accountService: AccountService, private router: Router) {
  }

  ngOnInit() {
    this.store.dispatch(new OrderActions.SetCheckoutStep(1));
    this.shippingForm = new FormGroup({
      shipAddress: new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'), Validators.required, BlankValidators.notBlankValidator, Validators.minLength(3), Validators.maxLength(240)]),
      billingAddress: new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(240)]),
      city: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), Validators.required, BlankValidators.notBlankValidator, Validators.minLength(3), Validators.maxLength(100)]),
      state: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), BlankValidators.checkIfBlankValidator, Validators.minLength(3), Validators.maxLength(40)]),
      zip: new FormControl(null, [Validators.required, Validators.pattern('^[0-9]*$'), Validators.maxLength(6), Validators.minLength(5)]),
      country: new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), Validators.required, BlankValidators.notBlankValidator, Validators.minLength(3), Validators.maxLength(40)])
    });

    this.accountService.getUser().pipe(take(1)).subscribe(data => {
      this.shippingForm.patchValue({
        shipAddress: data.address,
        city: data.city,
        state: data.state,
        zip: data.zip,
        country: data.country
      });

      this.innerLoading = false;
    });


    this.store.select('order').pipe(take(1)).subscribe((order: OrderState) => {
      if (order.shipping) {
        this.shippingForm.patchValue({
          shipAddress: order.shipping.shipAddress,
          city: order.shipping.city,
          state: order.shipping.state,
          zip: order.shipping.zip,
          country: order.shipping.country
        });
        this.innerLoading = false;
      } else {
        this.accountService.getUser().pipe(take(1)).subscribe((data: User) => {
          this.shippingForm.patchValue({
            shipAddress: data.address,
            city: data.city,
            state: data.state,
            zip: data.zip,
            country: data.country
          });
          this.innerLoading = false;
        });
      }

    });
  }

  onSubmitOrderForm() {
    const postData = {
      shipAddress: this.shippingForm.value.shipAddress.trim(),
      billingAddress: this.billingAddressEnabled ? this.shippingForm.value.billingAddress.trim() : null,
      city: this.shippingForm.value.city.trim(),
      state: this.shippingForm.value.state.trim(),
      zip: this.shippingForm.value.zip,
      country: this.shippingForm.value.country.trim()
    };
    this.store.dispatch(new OrderActions.PostShipping(postData));
    this.router.navigate(['/checkout/payment']);
  }

}
