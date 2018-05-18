import {Routes} from "@angular/router";
import {CheckoutComponent} from "./checkout.component";
import {AuthGuardService} from "../services/auth-guard.service";
import {OrderFormComponent} from "./order-form/order-form.component";
import {PaymentComponent} from "./payment/payment.component";
import {ConfirmationComponent} from "./confirmation/confirmation.component";
import {DisplayCartComponent} from "./display-cart/display-cart.component";
import {CheckoutGuardService} from "../services/checkout-guard.service";
import {SuccessComponent} from "./success/success.component";

export const CheckoutRoutes: Routes = [
  {
    path: 'checkout', component: CheckoutComponent,
    canActivate: [AuthGuardService],
    canDeactivate: [CheckoutGuardService],
    canActivateChild: [CheckoutGuardService],
    children: [
      {path: '', component: DisplayCartComponent},
      {path: 'form', component: OrderFormComponent},
      {path: 'payment', component: PaymentComponent},
      {path: 'confirm', component: ConfirmationComponent},
      {path: 'success', component: SuccessComponent}
    ]
  }
];
