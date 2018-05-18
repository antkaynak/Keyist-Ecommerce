import {Routes} from "@angular/router";
import {EmailVerificationComponent} from "./email-verification/email-verification.component";
import {EmailResetVerificationComponent} from "./email-reset-verification/email-reset-verification.component";
import {PasswordForgotVerificationComponent} from "./password-forgot-verification/password-forgot-verification.component";


export const VerificationRoutes: Routes = [
  {path: 'registrationConfirm', component: EmailVerificationComponent},
  {path: 'emailResetConfirm', component: EmailResetVerificationComponent},
  {path: 'passwordResetConfirm', component: PasswordForgotVerificationComponent}
];


// export const VerificationRoutes  : Routes = [
//   {path: '', component: VerificationComponent, children: [
//       { path: 'registrationConfirm', component: EmailVerificationComponent},
//       { path: 'emailResetConfirm', component: EmailResetVerificationComponent}
//     ]}
// ];
