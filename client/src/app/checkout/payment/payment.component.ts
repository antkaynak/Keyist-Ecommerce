import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as fromApp from '../../store/app.reducers';
import { Store } from '@ngrx/store';
import * as OrderActions from '../../store/order/order.actions';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {

  paymentForm: FormGroup;

  days: Array<number>;
  months: Array<number>;

  constructor(private store: Store<fromApp.AppState>, private router: Router) {
  }

  ngOnInit() {
    this.store.dispatch(new OrderActions.SetCheckoutStep(2));
    this.days = [...Array(12).keys()].map(i => i + 1);
    this.months = [...Array(20).keys()].map(i => i + 2019);
    this.paymentForm = new FormGroup({
      cardNo: new FormControl(123456789, Validators.required),
      cardOwner: new FormControl('DEMO', Validators.required),
      cardExp: new FormGroup({
        month: new FormControl(null, [Validators.required]),
        year: new FormControl(null, [Validators.required]),
      }),
      cardCCV: new FormControl(123, [Validators.required, Validators.maxLength(3), Validators.minLength(3)]),
    });
  }

  onSubmitOrderForm() {
    const paymentData: any = {
      cardOwner: this.paymentForm.value.cardOwner.toLocaleUpperCase(),
      cardNo: this.paymentForm.value.cardNo,
      cardExp: this.paymentForm.value.cardExp,
      cardCCV: this.paymentForm.value.cardCCV,
    };
    this.store.dispatch(new OrderActions.PostPayment(paymentData));
    this.router.navigate(['/checkout/confirm']);
  }
}
