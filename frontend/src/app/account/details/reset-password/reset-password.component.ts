import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../../services/account.service";
import * as PasswordValidators from "../../../services/validators/password.validator";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import * as BlankValidators from "../../../services/validators/blank.validator";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  resetPasswordForm: FormGroup;
  inlineLoading: boolean = false;

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.resetPasswordForm = new FormGroup({
      'oldPassword': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.minLength(6)]),
      'newPasswordGroup': new FormGroup({
        'newPassword': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.minLength(6)]),
        'newPasswordConfirm': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.minLength(6)])
      }, PasswordValidators.passwordMatchCheckValidator),
    });
  }

  onResetPasswordSubmit() {
    this.inlineLoading = true;
    this.accountService.resetPassword(
      this.resetPasswordForm.value.oldPassword,
      this.resetPasswordForm.value.newPasswordGroup.newPassword,
      this.resetPasswordForm.value.newPasswordGroup.newPasswordConfirm)
      .take(1)
      .catch(error => {
        this.inlineLoading = false;
        alert("Error resetting password. Please try again...");
        return Observable.throw(error);
      })
      .subscribe(res => {
        this.inlineLoading = false;
        alert("Success! Your password has been changed.");
        this.resetPasswordForm.reset();
      });
  }

}
