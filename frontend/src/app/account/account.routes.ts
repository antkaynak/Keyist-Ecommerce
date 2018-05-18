import {Routes} from "@angular/router";
import {AccountComponent} from "./account.component";
import {ListOrdersComponent} from "./list-orders/list-orders.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {AuthGuardService} from "../services/auth-guard.service";
import {DetailsComponent} from "./details/details.component";


export const AccountRoutes: Routes = [
  {
    path: '', component: AccountComponent, canActivate: [AuthGuardService], children: [
      {path: '', component: DashboardComponent},
      {path: 'dashboard', component: DashboardComponent},
      {path: 'orders', component: ListOrdersComponent},
      {path: 'details', component: DetailsComponent}
    ]
  }
];
