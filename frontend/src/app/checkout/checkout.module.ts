import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CheckoutComponent} from "./checkout.component";
import {RouterModule} from "@angular/router";
import {CheckoutRoutes} from "./checkout.routes";
import {InterestedComponent} from './display-cart/interested/interested.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {ReactiveFormsModule} from "@angular/forms";
import {PaymentComponent} from './payment/payment.component';
import {ConfirmationComponent} from './confirmation/confirmation.component';
import {OrderFormComponent} from './order-form/order-form.component';
import {DisplayCartComponent} from './display-cart/display-cart.component';
import {CheckoutGuardService} from "../services/checkout-guard.service";
import {BankAcceptComponent} from './confirmation/bank-accept/bank-accept.component';
import {SuccessComponent} from './success/success.component';
import {EmptyCartComponent} from './display-cart/empty-cart/empty-cart.component';


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(CheckoutRoutes),
    ReactiveFormsModule,
    NgbModule
  ],
  declarations: [CheckoutComponent, InterestedComponent,
    PaymentComponent, ConfirmationComponent, OrderFormComponent,
    DisplayCartComponent, BankAcceptComponent, SuccessComponent, EmptyCartComponent],
  providers: [CheckoutGuardService],
  bootstrap: [BankAcceptComponent]
})
export class CheckoutModule {
}
