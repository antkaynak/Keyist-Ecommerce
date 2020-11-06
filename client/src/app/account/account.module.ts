import { AuthGuardService } from './../services/auth-guard.service';
import { HelpComponent } from './help/help.component';
import { InformationComponent } from './information/information.component';
import { AddressComponent } from './address/address.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountComponent } from './account.component';
import { RouterModule } from '@angular/router';
import { AccountRoutes } from './account.routes';
import { ListOrdersComponent } from './list-orders/list-orders.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ResetPasswordComponent } from './information/reset-password/reset-password.component';
import { ResetDetailsComponent } from './information/reset-details/reset-details.component';


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AccountRoutes),
    ReactiveFormsModule,
    NgbModule
  ],
  declarations: [AccountComponent, ListOrdersComponent, AddressComponent, HelpComponent,
    InformationComponent, ResetPasswordComponent, ResetDetailsComponent],
  providers: [AuthGuardService]
})
export class AccountModule {
}
