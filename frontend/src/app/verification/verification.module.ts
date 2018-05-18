import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from "@angular/router";
import {VerificationRoutes} from "./verification.routes";
import {EmailVerificationComponent} from "./email-verification/email-verification.component";
import {EmailResetVerificationComponent} from './email-reset-verification/email-reset-verification.component';
import {PasswordForgotVerificationComponent} from './password-forgot-verification/password-forgot-verification.component';
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(VerificationRoutes),
    ReactiveFormsModule
  ],
  declarations: [EmailVerificationComponent, EmailResetVerificationComponent, PasswordForgotVerificationComponent]
})
export class VerificationModule {
}
