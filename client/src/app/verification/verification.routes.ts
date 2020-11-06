import { Routes } from '@angular/router';
import { EmailVerificationComponent } from './email-verification/email-verification.component';
import { PasswordForgotVerificationComponent } from './password-forgot-verification/password-forgot-verification.component';


export const VerificationRoutes: Routes = [
  { path: 'registrationConfirm', component: EmailVerificationComponent },
  { path: 'passwordResetConfirm', component: PasswordForgotVerificationComponent }
];
