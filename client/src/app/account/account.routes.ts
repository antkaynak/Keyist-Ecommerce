import { HelpComponent } from './help/help.component';
import { AddressComponent } from './address/address.component';
import { Routes } from '@angular/router';
import { AccountComponent } from './account.component';
import { ListOrdersComponent } from './list-orders/list-orders.component';
import { AuthGuardService } from '../services/auth-guard.service';
import { InformationComponent } from './information/information.component';


export const AccountRoutes: Routes = [
  {
    path: '', component: AccountComponent, canActivate: [AuthGuardService],
    children: [
      { path: '', redirectTo: 'information', pathMatch: 'full' },
      { path: 'information', component: InformationComponent },
      { path: 'orders', component: ListOrdersComponent },
      { path: 'address', component: AddressComponent },
      { path: 'help', component: HelpComponent }
    ]
  }
];
