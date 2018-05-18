import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import * as OrderActions from "../../store/order/order.actions";
import {PostOrdersObject} from "../../store/order/order.reducer";
import * as fromApp from "../../store/app.reducers";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import * as BlankValidators from "../../services/validators/blank.validator";
import {AccountService} from "../../services/account.service";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent implements OnInit {

  orderForm: FormGroup;
  emailPattern: string = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

  innerLoading: boolean = true;


  constructor(private store: Store<fromApp.AppState>, private accountService: AccountService, private router: Router) {
  }

  ngOnInit() {

    this.orderForm = new FormGroup({
      'shipName': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), Validators.required, BlankValidators.notBlankValidator]),
      'email': new FormControl(null, [Validators.required, Validators.pattern(this.emailPattern), BlankValidators.notBlankValidator]),
      'shipAddress': new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'), Validators.required, BlankValidators.notBlankValidator]),
      'shipAddress2': new FormControl(null, [Validators.pattern('[0-9a-zA-Z #,-]+'), BlankValidators.checkIfBlankValidator]),
      'city': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), Validators.required, BlankValidators.notBlankValidator]),
      'state': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), BlankValidators.checkIfBlankValidator]),
      'zip': new FormControl(null, [Validators.required, Validators.maxLength(6), Validators.minLength(5)]),
      'country': new FormControl(null, [Validators.pattern('^[a-zA-Z\\s]+$'), Validators.required, BlankValidators.notBlankValidator]),
      'phone': new FormControl(null, [Validators.required, Validators.pattern('[0-9]+'), Validators.minLength(11), Validators.maxLength(12)]),
      'cargoFirm': new FormControl(null, [Validators.required, BlankValidators.notBlankValidator]),
    });

    this.accountService.getUser().take(1).subscribe(data => {
      this.orderForm.patchValue({
        shipName: data.firstName + ' ' + data.lastName,
        email: data.email,
        shipAddress: data.address,
        shipAddress2: data.address2,
        city: data.city,
        state: data.state,
        zip: data.zip,
        country: data.country,
        phone: data.phone,
        cargoFirm: 'ups'
      });

      this.innerLoading = false;
    });

  }


  onSubmitOrderForm() {
    console.log(this.orderForm);
    const postData: PostOrdersObject = {
      shipName: this.orderForm.value.shipName.trim(),
      email: this.orderForm.value.email.trim(),
      shipAddress: this.orderForm.value.shipAddress.trim(),
      shipAddress2: this.orderForm.value.shipAddress2 == null ? null : this.orderForm.value.shipAddress2.trim(),
      city: this.orderForm.value.city.trim(),
      state: this.orderForm.value.state.trim(),
      zip: this.orderForm.value.zip,
      country: this.orderForm.value.country.trim(),
      phone: this.orderForm.value.phone,
      cargoFirm: this.orderForm.value.cargoFirm
    };
    console.log(postData);
    console.log('Posting order from form');
    this.store.dispatch(new OrderActions.PostOrderForm(postData));
    this.router.navigate(["/checkout/payment"]);
  }
}
