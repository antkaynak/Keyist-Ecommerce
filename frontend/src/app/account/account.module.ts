import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountComponent} from "./account.component";
import {RouterModule} from "@angular/router";
import {AccountRoutes} from "./account.routes";
import {ListOrdersComponent} from './list-orders/list-orders.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {DetailsComponent} from './details/details.component';
import {ReactiveFormsModule} from "@angular/forms";
import {ResetPasswordComponent} from './details/reset-password/reset-password.component';
import {ResetEmailComponent} from './details/reset-email/reset-email.component';
import {ResetDetailsComponent} from './details/reset-details/reset-details.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AccountRoutes),
    ReactiveFormsModule,
    NgbModule
  ],
  declarations: [AccountComponent, ListOrdersComponent, DashboardComponent,
    DetailsComponent, ResetPasswordComponent, ResetEmailComponent, ResetDetailsComponent]
})
export class AccountModule {
}
