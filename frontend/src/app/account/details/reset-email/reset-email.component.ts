import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import * as BlankValidators from "../../../services/validators/blank.validator";
import {AccountService} from "../../../services/account.service";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-reset-email',
  templateUrl: './reset-email.component.html',
  styleUrls: ['./reset-email.component.css']
})
export class ResetEmailComponent implements OnInit {

  resetEmailForm: FormGroup;
  emailPattern: string = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
  inlineLoading: boolean = false;

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.resetEmailForm = new FormGroup({
      'newEmail': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.pattern(this.emailPattern)]),
      'newEmailConfirm': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.pattern(this.emailPattern)]),
      'password': new FormControl(null, [Validators.required, BlankValidators.checkIfBlankValidator, Validators.minLength(6)]),
    });
  }

  onResetEmailSubmit() {
    this.inlineLoading = true;
    this.accountService.resetEmail(this.resetEmailForm.value.newEmail, this.resetEmailForm.value.newEmailConfirm, this.resetEmailForm.value.password)
      .take(1)
      .catch(error => {
        console.log("wer");
        this.inlineLoading = false;
        if (error.status === 409) {
          alert("An account with this email already exists!");
          return;
        }
        alert("Error resetting email. Please try again...");
        return Observable.throw(error);
      })
      .subscribe(res => {
        this.inlineLoading = false;
        alert("Success! A link has been sent to " + this.resetEmailForm.value.newEmail +
          "\n\n Please check your email to confirm.");
        this.resetEmailForm.reset();
      });
  }

}
