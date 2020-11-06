import { PersonalComponent } from './personal/personal.component';
import { ShippingComponent } from './shipping/shipping.component';
import { Routes } from '@angular/router';
import { CheckoutComponent } from './checkout.component';
import { AuthGuardService } from '../services/auth-guard.service';
import { PaymentComponent } from './payment/payment.component';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { CheckoutGuardService } from '../services/checkout-guard.service';
import { SuccessComponent } from './success/success.component';

export const CheckoutRoutes: Routes = [
  {
    path: '', component: CheckoutComponent,
    canActivate: [AuthGuardService, CheckoutGuardService],
    canDeactivate: [CheckoutGuardService],
    canActivateChild: [CheckoutGuardService],
    children: [
      { path: '', redirectTo: 'personal', pathMatch: 'full' },
      { path: 'personal', component: PersonalComponent },
      { path: 'shipping', component: ShippingComponent },
      { path: 'payment', component: PaymentComponent },
      { path: 'confirm', component: ConfirmationComponent }
    ]
  },
  {
    path: 'success', component: SuccessComponent,
    canActivate: [AuthGuardService],

  }
];
