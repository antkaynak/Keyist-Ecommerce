import { SigninComponent } from './signin/signin.component';
import { SignupComponent } from './signup/signup.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { NonAuthGuardService } from '../services/non-auth-guard.service';

export const AuthRoutes = [
  { path: 'login', component: SigninComponent, canActivate: [NonAuthGuardService] },
  { path: 'signup', component: SignupComponent, canActivate: [NonAuthGuardService] },
  { path: 'forgot-password', component: ForgotPasswordComponent, canActivate: [NonAuthGuardService] }
];
