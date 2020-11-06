import { TermsComponent } from './summary/terms/terms.component';
import { PersonalComponent } from './personal/personal.component';
import { SummaryComponent } from './summary/summary.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CheckoutComponent } from './checkout.component';
import { RouterModule } from '@angular/router';
import { CheckoutRoutes } from './checkout.routes';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';
import { PaymentComponent } from './payment/payment.component';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { CheckoutGuardService } from '../services/checkout-guard.service';
import { BankAcceptComponent } from './summary/bank-accept/bank-accept.component';
import { SuccessComponent } from './success/success.component';
import { ProgressBarComponent } from './progress-bar/progress-bar.component';
import { ShippingComponent } from './shipping/shipping.component';


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(CheckoutRoutes),
    ReactiveFormsModule,
    NgbModule
  ],
  declarations: [CheckoutComponent, ProgressBarComponent, ShippingComponent, SummaryComponent,
    PaymentComponent, ConfirmationComponent, PersonalComponent,
    BankAcceptComponent, TermsComponent, SuccessComponent],
  providers: [CheckoutGuardService],
  bootstrap: [BankAcceptComponent, TermsComponent]
})
export class CheckoutModule {
}
