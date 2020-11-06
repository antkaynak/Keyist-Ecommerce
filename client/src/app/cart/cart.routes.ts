import { CartComponent } from './cart.component';
import { Routes } from '@angular/router';
import { AuthGuardService } from '../services/auth-guard.service';

export const CartRoutes: Routes = [
  {
    path: '', component: CartComponent,
    canActivate: [AuthGuardService]
  }
];
